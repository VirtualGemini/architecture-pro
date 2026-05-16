package com.velox.infrastructure.web.file.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.velox.common.exception.ApiException;
import com.velox.common.exception.BusinessErrorCode;
import com.velox.common.result.PageResult;
import com.velox.domain.model.FileConfig;
import com.velox.infrastructure.framework.file.core.client.FileClient;
import com.velox.infrastructure.framework.file.core.client.FileClientConfig;
import com.velox.infrastructure.framework.file.core.client.FileClientFactory;
import com.velox.infrastructure.framework.file.core.enums.FileStorageEnum;
import com.velox.infrastructure.id.BusinessIdGenerator;
import com.velox.infrastructure.persistence.FileConfigMapper;
import com.velox.infrastructure.web.RequestDateTimeFormatter;
import com.velox.infrastructure.web.file.service.FileConfigService;
import com.velox.infrastructure.web.file.vo.FileConfigPageReqVO;
import com.velox.infrastructure.web.file.vo.FileConfigRespVO;
import com.velox.infrastructure.web.file.vo.FileConfigSaveReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Validated
public class FileConfigServiceImpl implements FileConfigService {

    private static final String CACHE_MASTER_ID = "master";

    private final LoadingCache<String, FileClient> clientCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(Duration.ofSeconds(10L))
            .build(new CacheLoader<>() {
                @Override
                public FileClient load(String id) {
                    FileConfig config = Objects.equals(CACHE_MASTER_ID, id) ?
                            fileConfigMapper.selectByMaster() : fileConfigMapper.selectById(id);
                    if (config != null) {
                        FileClientConfig clientConfig = parseClientConfig(config.getStorage(), config.getConfig());
                        fileClientFactory.createOrUpdateFileClient(config.getId(), config.getStorage(), clientConfig);
                    }
                    return fileClientFactory.getFileClient(null == config ? id : config.getId());
                }
            });

    private final FileClientFactory fileClientFactory;

    private final FileConfigMapper fileConfigMapper;

    private final Validator validator;

    private final BusinessIdGenerator idGenerator;

    public FileConfigServiceImpl(FileClientFactory fileClientFactory,
                                 FileConfigMapper fileConfigMapper,
                                 Validator validator,
                                 BusinessIdGenerator idGenerator) {
        this.fileClientFactory = fileClientFactory;
        this.fileConfigMapper = fileConfigMapper;
        this.validator = validator;
        this.idGenerator = idGenerator;
    }

    public LoadingCache<String, FileClient> getClientCache() {
        return clientCache;
    }

    @Override
    public String createFileConfig(FileConfigSaveReqVO createReqVO) {
        validateClientConfig(createReqVO.getStorage(), createReqVO.getConfig());
        FileConfig fileConfig = new FileConfig();
        fileConfig.setId(idGenerator.nextFileConfigId());
        fileConfig.setName(createReqVO.getName());
        fileConfig.setStorage(createReqVO.getStorage());
        fileConfig.setConfig(createReqVO.getConfig());
        fileConfig.setRemark(createReqVO.getRemark());
        fileConfig.setMaster(false);
        fileConfig.setEnabled(createReqVO.getEnabled() != null ? createReqVO.getEnabled() : 1);
        fileConfigMapper.insert(fileConfig);
        return fileConfig.getId();
    }

    @Override
    public void updateFileConfig(FileConfigSaveReqVO updateReqVO) {
        FileConfig config = validateFileConfigExists(updateReqVO.getId());
        validateClientConfig(config.getStorage(), updateReqVO.getConfig());
        FileConfig updateObj = new FileConfig();
        updateObj.setId(updateReqVO.getId());
        updateObj.setName(updateReqVO.getName());
        updateObj.setStorage(updateReqVO.getStorage());
        updateObj.setConfig(updateReqVO.getConfig());
        updateObj.setRemark(updateReqVO.getRemark());
        updateObj.setMaster(updateReqVO.getMaster());
        updateObj.setEnabled(updateReqVO.getEnabled());
        fileConfigMapper.updateById(updateObj);
        clearCache(config.getId(), null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFileConfigMaster(String id) {
        validateFileConfigExists(id);
        fileConfigMapper.updateNoneMaster();
        FileConfig updateObj = new FileConfig();
        updateObj.setId(id);
        updateObj.setMaster(true);
        fileConfigMapper.updateById(updateObj);
        clearCache(null, true);
    }

    @Override
    public void updateFileConfigEnabled(String id, Integer enabled) {
        validateFileConfigExists(id);
        FileConfig updateObj = new FileConfig();
        updateObj.setId(id);
        updateObj.setEnabled(enabled);
        fileConfigMapper.updateById(updateObj);
        clearCache(id, null);
    }

    private void validateClientConfig(Integer storage, String config) {
        FileClientConfig clientConfig = parseClientConfig(storage, config);
        Set<ConstraintViolation<FileClientConfig>> violations = validator.validate(clientConfig);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .distinct()
                    .reduce((left, right) -> left + "; " + right)
                    .orElse("文件配置不合法");
            throw new ApiException(BusinessErrorCode.FILE_CONFIG_INVALID, message);
        }
    }

    private FileClientConfig parseClientConfig(Integer storage, String config) {
        FileStorageEnum storageEnum = FileStorageEnum.getByStorage(storage);
        if (storageEnum == null) {
            throw new ApiException(BusinessErrorCode.FILE_STORAGE_TYPE_UNSUPPORTED, storage);
        }
        Class<? extends FileClientConfig> configClass = storageEnum.getConfigClass();
        return JSONUtil.toBean(config, configClass);
    }

    @Override
    public void deleteFileConfig(String id) {
        FileConfig config = validateFileConfigExists(id);
        if (Boolean.TRUE.equals(config.getMaster())) {
            throw new ApiException(BusinessErrorCode.FILE_CONFIG_DELETE_FAIL_MASTER);
        }
        fileConfigMapper.deleteById(id);
        clearCache(id, null);
    }

    @Override
    public void deleteFileConfigList(List<String> ids) {
        List<FileConfig> configs = fileConfigMapper.selectByIds(ids);
        for (FileConfig config : configs) {
            if (Boolean.TRUE.equals(config.getMaster())) {
                throw new ApiException(BusinessErrorCode.FILE_CONFIG_DELETE_FAIL_MASTER);
            }
        }
        fileConfigMapper.deleteByIds(ids);
        ids.forEach(id -> clearCache(id, null));
    }

    private void clearCache(String id, Boolean master) {
        if (id != null) {
            clientCache.invalidate(id);
        }
        if (Boolean.TRUE.equals(master)) {
            clientCache.invalidate(CACHE_MASTER_ID);
        }
    }

    private FileConfig validateFileConfigExists(String id) {
        FileConfig config = fileConfigMapper.selectById(id);
        if (config == null) {
            throw new ApiException(BusinessErrorCode.FILE_CONFIG_NOT_FOUND);
        }
        return config;
    }

    @Override
    public FileConfigRespVO getFileConfig(String id) {
        return toFileConfigRespVO(fileConfigMapper.selectById(id));
    }

    @Override
    public PageResult<FileConfigRespVO> getFileConfigPage(FileConfigPageReqVO pageReqVO) {
        LambdaQueryWrapper<FileConfig> wrapper = new LambdaQueryWrapper<FileConfig>()
                .like(StrUtil.isNotEmpty(pageReqVO.getName()), FileConfig::getName, pageReqVO.getName())
                .eq(pageReqVO.getStorage() != null, FileConfig::getStorage, pageReqVO.getStorage())
                .orderByDesc(FileConfig::getCreateTime)
                .orderByDesc(FileConfig::getUpdateTime);
        Page<FileConfig> page = fileConfigMapper.selectPage(
                new Page<>(pageReqVO.getPage(), pageReqVO.getSize()), wrapper);
        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(),
                page.getRecords().stream().map(this::toFileConfigRespVO).toList());
    }

    @Override
    public String testFileConfig(String id) throws Exception {
        validateFileConfigExists(id);
        byte[] content = "test".getBytes();
        return getFileClient(id).upload(content, IdUtil.fastSimpleUUID() + ".txt", "text/plain");
    }

    @Override
    public FileClient getFileClient(String id) {
        return clientCache.getUnchecked(id);
    }

    @Override
    public FileClient getMasterFileClient() {
        return clientCache.getUnchecked(CACHE_MASTER_ID);
    }

    private FileConfigRespVO toFileConfigRespVO(FileConfig fileConfig) {
        if (fileConfig == null) {
            return null;
        }
        FileConfigRespVO respVO = new FileConfigRespVO();
        respVO.setId(fileConfig.getId());
        respVO.setName(fileConfig.getName());
        respVO.setStorage(fileConfig.getStorage());
        respVO.setConfig(fileConfig.getConfig());
        respVO.setMaster(fileConfig.getMaster());
        respVO.setRemark(fileConfig.getRemark());
        respVO.setEnabled(fileConfig.getEnabled());
        respVO.setCreateBy(fileConfig.getCreateBy());
        respVO.setCreateTime(RequestDateTimeFormatter.format(fileConfig.getCreateTime()));
        respVO.setUpdateTime(RequestDateTimeFormatter.format(fileConfig.getUpdateTime()));
        return respVO;
    }
}

package com.velox.infrastructure.web.file.service;

import com.velox.common.result.PageResult;
import com.velox.infrastructure.framework.file.core.client.FileClient;
import com.velox.infrastructure.web.file.vo.FileConfigPageReqVO;
import com.velox.infrastructure.web.file.vo.FileConfigRespVO;
import com.velox.infrastructure.web.file.vo.FileConfigSaveReqVO;
import jakarta.validation.Valid;

import java.util.List;

public interface FileConfigService {

    String createFileConfig(@Valid FileConfigSaveReqVO createReqVO);

    void updateFileConfig(@Valid FileConfigSaveReqVO updateReqVO);

    void updateFileConfigMaster(String id);

    void updateFileConfigEnabled(String id, Integer enabled);

    void deleteFileConfig(String id);

    void deleteFileConfigList(List<String> ids);

    FileConfigRespVO getFileConfig(String id);

    PageResult<FileConfigRespVO> getFileConfigPage(FileConfigPageReqVO pageReqVO);

    String testFileConfig(String id) throws Exception;

    FileClient getFileClient(String id);

    FileClient getMasterFileClient();
}

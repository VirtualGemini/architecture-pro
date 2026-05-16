package com.velox.infrastructure.framework.file.core.client.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.velox.domain.model.FileContent;
import com.velox.infrastructure.framework.file.core.client.AbstractFileClient;
import com.velox.infrastructure.id.BusinessIdGenerator;
import com.velox.infrastructure.persistence.FileContentMapper;

import java.util.Comparator;
import java.util.List;

public class DBFileClient extends AbstractFileClient<DBFileClientConfig> {

    private FileContentMapper fileContentMapper;

    private BusinessIdGenerator businessIdGenerator;

    public DBFileClient(String id, DBFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        fileContentMapper = SpringUtil.getBean(FileContentMapper.class);
        businessIdGenerator = SpringUtil.getBean(BusinessIdGenerator.class);
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        FileContent contentDO = new FileContent();
        contentDO.setId(businessIdGenerator.nextFileContentId());
        contentDO.setConfigId(getId());
        contentDO.setPath(path);
        contentDO.setContent(content);
        fileContentMapper.insert(contentDO);
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public void delete(String path) {
        fileContentMapper.deleteByConfigIdAndPath(getId(), path);
    }

    @Override
    public byte[] getContent(String path) {
        List<FileContent> list = fileContentMapper.selectListByConfigIdAndPath(getId(), path);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        list.sort(Comparator
                .comparing(FileContent::getCreateTime, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(FileContent::getUpdateTime, Comparator.nullsFirst(Comparator.naturalOrder())));
        return CollUtil.getLast(list).getContent();
    }
}

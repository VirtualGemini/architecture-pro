package com.architecturepro.infrastructure.framework.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import com.architecturepro.infrastructure.framework.file.core.client.FileClient;
import com.architecturepro.infrastructure.framework.file.core.client.FileClientConfig;
import com.architecturepro.infrastructure.framework.file.core.client.db.DBFileClient;
import com.architecturepro.infrastructure.framework.file.core.client.db.DBFileClientConfig;
import com.architecturepro.infrastructure.framework.file.core.client.ftp.FtpFileClient;
import com.architecturepro.infrastructure.framework.file.core.client.ftp.FtpFileClientConfig;
import com.architecturepro.infrastructure.framework.file.core.client.local.LocalFileClient;
import com.architecturepro.infrastructure.framework.file.core.client.local.LocalFileClientConfig;
import com.architecturepro.infrastructure.framework.file.core.client.s3.S3FileClient;
import com.architecturepro.infrastructure.framework.file.core.client.s3.S3FileClientConfig;
import com.architecturepro.infrastructure.framework.file.core.client.sftp.SftpFileClient;
import com.architecturepro.infrastructure.framework.file.core.client.sftp.SftpFileClientConfig;

public enum FileStorageEnum {

    DB(1, DBFileClientConfig.class, DBFileClient.class),

    LOCAL(10, LocalFileClientConfig.class, LocalFileClient.class),
    FTP(11, FtpFileClientConfig.class, FtpFileClient.class),
    SFTP(12, SftpFileClientConfig.class, SftpFileClient.class),

    S3(20, S3FileClientConfig.class, S3FileClient.class),
    ;

    private final Integer storage;
    private final Class<? extends FileClientConfig> configClass;
    private final Class<? extends FileClient> clientClass;

    FileStorageEnum(Integer storage, Class<? extends FileClientConfig> configClass, Class<? extends FileClient> clientClass) {
        this.storage = storage;
        this.configClass = configClass;
        this.clientClass = clientClass;
    }

    public Integer getStorage() {
        return storage;
    }

    public Class<? extends FileClientConfig> getConfigClass() {
        return configClass;
    }

    public Class<? extends FileClient> getClientClass() {
        return clientClass;
    }

    public static FileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }
}

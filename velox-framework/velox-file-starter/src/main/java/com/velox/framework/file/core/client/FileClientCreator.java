package com.velox.framework.file.core.client;

@FunctionalInterface
public interface FileClientCreator {

    FileClient create(String configId, FileClientConfig config);
}

package com.velox.infrastructure.framework.file.core.client;

public interface FileClientFactory {

    FileClient getFileClient(String configId);

    <Config extends FileClientConfig> void createOrUpdateFileClient(String configId, Integer storage, Config config);

}

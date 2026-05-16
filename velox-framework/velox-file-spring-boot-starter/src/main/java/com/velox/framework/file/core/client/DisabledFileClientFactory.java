package com.velox.framework.file.core.client;

public class DisabledFileClientFactory implements FileClientFactory {

    @Override
    public FileClient getFileClient(String configId) {
        return new DisabledFileClient(configId != null ? configId : "disabled");
    }

    @Override
    public <Config extends FileClientConfig> void createOrUpdateFileClient(String configId, Integer storage, Config config) {
        // Disabled mode keeps API available but never initializes real clients.
    }

    @Override
    public Class<? extends FileClientConfig> getConfigClass(Integer storage) {
        return null;
    }
}

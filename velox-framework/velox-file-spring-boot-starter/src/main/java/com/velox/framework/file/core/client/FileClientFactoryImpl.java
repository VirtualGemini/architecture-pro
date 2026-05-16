package com.velox.framework.file.core.client;

import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileClientFactoryImpl implements FileClientFactory {

    private static final Logger log = LoggerFactory.getLogger(FileClientFactoryImpl.class);

    private final ConcurrentMap<String, AbstractFileClient<?>> clients = new ConcurrentHashMap<>();
    private final FileClientTypeRegistry typeRegistry;

    public FileClientFactoryImpl(FileClientTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    @Override
    public FileClient getFileClient(String configId) {
        AbstractFileClient<?> client = clients.get(configId);
        if (client == null) {
            log.error("[getFileClient][配置编号({}) 找不到客户端]", configId);
        }
        return client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Config extends FileClientConfig> void createOrUpdateFileClient(String configId, Integer storage, Config config) {
        AbstractFileClient<Config> client = (AbstractFileClient<Config>) clients.get(configId);
        if (client == null) {
            client = this.createFileClient(configId, storage, config);
            client.init();
            clients.put(client.getId(), client);
        } else {
            client.refresh(config);
        }
    }

    @Override
    public Class<? extends FileClientConfig> getConfigClass(Integer storage) {
        FileClientTypeRegistration registration = typeRegistry.get(storage);
        return registration != null ? registration.configClass() : null;
    }

    @SuppressWarnings("unchecked")
    private <Config extends FileClientConfig> AbstractFileClient<Config> createFileClient(
            String configId, Integer storage, Config config) {
        FileClientTypeRegistration registration = typeRegistry.get(storage);
        Assert.notNull(registration, String.format("Unsupported file storage type: %s", storage));
        return (AbstractFileClient<Config>) registration.creator().create(configId, config);
    }
}

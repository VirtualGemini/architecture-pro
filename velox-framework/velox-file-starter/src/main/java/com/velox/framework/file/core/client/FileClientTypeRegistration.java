package com.velox.framework.file.core.client;

public record FileClientTypeRegistration(
        Integer storage,
        Class<? extends FileClientConfig> configClass,
        FileClientCreator creator
) {
}

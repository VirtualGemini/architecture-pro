package com.velox.infrastructure.framework.file.core.client;

public interface FileClient {

    String getId();

    String upload(byte[] content, String path, String type) throws Exception;

    void delete(String path) throws Exception;

    byte[] getContent(String path) throws Exception;

    default String presignPutUrl(String path) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    default String presignGetUrl(String url, Integer expirationSeconds) {
        throw new UnsupportedOperationException("不支持的操作");
    }
}

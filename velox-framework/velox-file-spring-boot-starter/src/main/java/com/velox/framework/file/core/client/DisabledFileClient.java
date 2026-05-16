package com.velox.framework.file.core.client;

public class DisabledFileClient implements FileClient {

    private final String id;

    public DisabledFileClient(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        throw disabledException();
    }

    @Override
    public void delete(String path) {
        throw disabledException();
    }

    @Override
    public byte[] getContent(String path) {
        throw disabledException();
    }

    @Override
    public String presignPutUrl(String path) {
        throw disabledException();
    }

    @Override
    public String presignGetUrl(String url, Integer expirationSeconds) {
        throw disabledException();
    }

    private ApiException disabledException() {
        return new ApiException("FILE_SERVICE_DISABLED",
                "File capability is disabled. Please enable velox.file.enabled=true");
    }

    public static final class ApiException extends RuntimeException {

        private final String code;

        public ApiException(String code, String message) {
            super(message);
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}

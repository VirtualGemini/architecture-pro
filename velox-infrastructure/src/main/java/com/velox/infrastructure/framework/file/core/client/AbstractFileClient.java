package com.velox.infrastructure.framework.file.core.client;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

public abstract class AbstractFileClient<Config extends FileClientConfig> implements FileClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractFileClient.class);

    private final String id;
    protected Config config;
    private Config originalConfig;

    public AbstractFileClient(String id, Config config) {
        this.id = id;
        this.config = config;
        this.originalConfig = config;
    }

    public final void init() {
        doInit();
        log.debug("[init][配置({}) 初始化完成]", config);
    }

    protected abstract void doInit();

    public final void refresh(Config config) {
        if (config.equals(this.originalConfig)) {
            return;
        }
        log.info("[refresh][配置({})发生变化，重新初始化]", config);
        this.config = config;
        this.originalConfig = config;
        this.init();
    }

    @Override
    public String getId() {
        return id;
    }

    protected String formatFileUrl(String domain, String path) {
        String relativePath = StrUtil.format("/api/file/{}/get/{}", getId(), encodePath(path));
        if (StrUtil.isBlank(domain)) {
            return relativePath;
        }
        String normalizedDomain = StrUtil.removeSuffix(domain, "/");
        return normalizedDomain + relativePath;
    }

    private String encodePath(String path) {
        return StrUtil.splitTrim(path, StrUtil.SLASH).stream()
                .map(item -> UriUtils.encodePathSegment(item, StandardCharsets.UTF_8))
                .reduce((left, right) -> left + StrUtil.SLASH + right)
                .orElse("");
    }
}

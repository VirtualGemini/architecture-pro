package com.velox.framework.redis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Redis 缓存扩展配置
 */
@Validated
@ConfigurationProperties("velox.cache")
public class VeloxCacheProperties {

    /**
     * Redis scan 一次返回数量
     */
    private Integer redisScanBatchSize = 30;

    public Integer getRedisScanBatchSize() {
        return redisScanBatchSize;
    }

    public void setRedisScanBatchSize(Integer redisScanBatchSize) {
        this.redisScanBatchSize = redisScanBatchSize;
    }
}

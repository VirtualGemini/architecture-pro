package com.velox.module.infra.persistence.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 业务级 Mapper 扫描策略
 */
@Configuration
@MapperScan({
        "com.velox.module.system.persistence",
        "com.velox.module.system.file.persistence"
})
public class VeloxInfraPersistenceMapperScanConfiguration {
}

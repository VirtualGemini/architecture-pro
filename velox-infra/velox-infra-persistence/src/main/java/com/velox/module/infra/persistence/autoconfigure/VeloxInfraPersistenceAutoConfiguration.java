package com.velox.module.infra.persistence.autoconfigure;

import com.velox.module.infra.persistence.config.VeloxPersistenceMapperScanConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(VeloxPersistenceMapperScanConfiguration.class)
public class VeloxInfraPersistenceAutoConfiguration {
}

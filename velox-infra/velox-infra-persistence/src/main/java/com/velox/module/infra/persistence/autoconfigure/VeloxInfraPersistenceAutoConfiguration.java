package com.velox.module.infra.persistence.autoconfigure;

import com.velox.module.infra.persistence.config.VeloxInfraPersistenceMapperScanConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(VeloxInfraPersistenceMapperScanConfiguration.class)
public class VeloxInfraPersistenceAutoConfiguration {
}

package com.velox.module.bootstrap.persistence.autoconfigure;

import com.velox.module.bootstrap.persistence.config.VeloxBootstrapPersistenceMapperScanConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(VeloxBootstrapPersistenceMapperScanConfiguration.class)
public class VeloxBootstrapPersistenceAutoConfiguration {
}

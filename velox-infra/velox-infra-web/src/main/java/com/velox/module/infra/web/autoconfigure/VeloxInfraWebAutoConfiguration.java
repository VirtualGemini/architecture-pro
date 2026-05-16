package com.velox.module.infra.web.autoconfigure;

import com.velox.module.infra.web.config.VeloxWebMvcConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(VeloxWebMvcConfiguration.class)
public class VeloxInfraWebAutoConfiguration {
}

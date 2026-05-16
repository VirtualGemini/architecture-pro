package com.velox.framework.context.autoconfigure;

import com.velox.framework.config.VeloxProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(VeloxProperties.class)
public class VeloxContextAutoConfiguration {
}

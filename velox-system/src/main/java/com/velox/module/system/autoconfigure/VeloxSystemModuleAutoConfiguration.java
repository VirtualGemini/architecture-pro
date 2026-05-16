package com.velox.module.system.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = "com.velox.module.system")
public class VeloxSystemModuleAutoConfiguration {
}

package com.velox.framework.security.autoconfigure;

import com.velox.framework.config.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(SecurityProperties.class)
public class VeloxSecurityAutoConfiguration {
}

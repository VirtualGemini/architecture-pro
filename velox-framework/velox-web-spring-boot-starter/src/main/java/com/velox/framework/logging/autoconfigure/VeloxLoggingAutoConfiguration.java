package com.velox.framework.logging.autoconfigure;

import com.velox.framework.log.RequestLogInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(RequestLogInterceptor.class)
public class VeloxLoggingAutoConfiguration {
}

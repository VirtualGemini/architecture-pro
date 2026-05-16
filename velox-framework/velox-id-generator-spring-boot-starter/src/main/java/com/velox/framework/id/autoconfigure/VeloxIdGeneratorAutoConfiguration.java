package com.velox.framework.id.autoconfigure;

import com.velox.framework.id.BusinessIdGenerator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(BusinessIdGenerator.class)
public class VeloxIdGeneratorAutoConfiguration {
}

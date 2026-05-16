package com.velox.infrastructure.framework.file.config;

import cn.hutool.extra.spring.SpringUtil;
import com.velox.infrastructure.framework.file.core.client.FileClientFactory;
import com.velox.infrastructure.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class FileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }
}

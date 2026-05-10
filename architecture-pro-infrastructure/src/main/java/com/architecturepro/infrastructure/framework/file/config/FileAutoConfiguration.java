package com.architecturepro.infrastructure.framework.file.config;

import cn.hutool.extra.spring.SpringUtil;
import com.architecturepro.infrastructure.framework.file.core.client.FileClientFactory;
import com.architecturepro.infrastructure.framework.file.core.client.FileClientFactoryImpl;
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

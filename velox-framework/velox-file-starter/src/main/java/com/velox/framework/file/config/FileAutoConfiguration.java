package com.velox.framework.file.config;

import cn.hutool.extra.spring.SpringUtil;
import com.velox.framework.file.core.client.DisabledFileClientFactory;
import com.velox.framework.file.core.client.FileClientFactory;
import com.velox.framework.file.core.client.FileClientFactoryImpl;
import com.velox.framework.file.core.client.FileClientTypeRegistration;
import com.velox.framework.file.core.client.FileClientTypeRegistry;
import com.velox.framework.file.core.client.ftp.FtpFileClient;
import com.velox.framework.file.core.client.ftp.FtpFileClientConfig;
import com.velox.framework.file.core.client.local.LocalFileClient;
import com.velox.framework.file.core.client.local.LocalFileClientConfig;
import com.velox.framework.file.core.client.s3.S3FileClient;
import com.velox.framework.file.core.client.s3.S3FileClientConfig;
import com.velox.framework.file.core.client.sftp.SftpFileClient;
import com.velox.framework.file.core.client.sftp.SftpFileClientConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileClientTypeRegistry fileClientTypeRegistry(List<FileClientTypeRegistration> registrations) {
        List<FileClientTypeRegistration> allRegistrations = new ArrayList<>();
        allRegistrations.add(new FileClientTypeRegistration(10, LocalFileClientConfig.class,
                (configId, config) -> new LocalFileClient(configId, (LocalFileClientConfig) config)));
        allRegistrations.add(new FileClientTypeRegistration(11, FtpFileClientConfig.class,
                (configId, config) -> new FtpFileClient(configId, (FtpFileClientConfig) config)));
        allRegistrations.add(new FileClientTypeRegistration(12, SftpFileClientConfig.class,
                (configId, config) -> new SftpFileClient(configId, (SftpFileClientConfig) config)));
        allRegistrations.add(new FileClientTypeRegistration(20, S3FileClientConfig.class,
                (configId, config) -> new S3FileClient(configId, (S3FileClientConfig) config)));
        allRegistrations.addAll(registrations);
        return new FileClientTypeRegistry(allRegistrations);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "velox.file", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FileClientFactory fileClientFactory(FileClientTypeRegistry fileClientTypeRegistry) {
        return new FileClientFactoryImpl(fileClientTypeRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "velox.file", name = "enabled", havingValue = "false")
    public FileClientFactory disabledFileClientFactory() {
        return new DisabledFileClientFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }
}

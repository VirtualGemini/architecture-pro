package com.architecturepro.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 应用启动入口
 */
@SpringBootApplication(scanBasePackages = "com.architecturepro")
public class ArchitectureProApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ArchitectureProApplication.class);
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);
    }
}

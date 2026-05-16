package com.velox.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 应用启动入口
 */
@SpringBootApplication(scanBasePackages = "com.velox")
public class VeloxApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(VeloxApplication.class);
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);
    }
}

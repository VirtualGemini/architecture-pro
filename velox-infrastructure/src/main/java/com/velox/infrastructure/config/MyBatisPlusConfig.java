package com.velox.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * MyBatis Plus 配置
 */
@Configuration
@MapperScan({"com.velox.**.db", "com.velox.infrastructure.persistence"})
public class MyBatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(VeloxDataSourceProperties properties,
                                                         DatabaseDialectRegistry dialectRegistry) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DbType dbType = dialectRegistry.getRequiredDialect(properties.getType()).getMybatisDbType();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
        return interceptor;
    }

    /**
     * 自动填充处理器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
                this.setFieldValByName("updateTime", now, metaObject);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.setFieldValByName("updateTime", LocalDateTime.now(ZoneOffset.UTC), metaObject);
            }
        };
    }
}

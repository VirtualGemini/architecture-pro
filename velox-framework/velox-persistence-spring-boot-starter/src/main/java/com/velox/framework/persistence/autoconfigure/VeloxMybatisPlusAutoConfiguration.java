package com.velox.framework.persistence.autoconfigure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.velox.framework.persistence.config.DatabaseDialectRegistry;
import com.velox.framework.persistence.config.VeloxDataSourceProperties;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * MyBatis Plus 自动装配
 */
@AutoConfiguration
@ConditionalOnClass(MybatisPlusInterceptor.class)
public class VeloxMybatisPlusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(VeloxDataSourceProperties properties,
                                                         DatabaseDialectRegistry dialectRegistry) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(
                dialectRegistry.getRequiredDialect(properties.getType()).getMybatisDbType()
        ));
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
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

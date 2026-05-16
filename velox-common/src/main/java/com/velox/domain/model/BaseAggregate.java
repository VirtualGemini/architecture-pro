package com.velox.domain.model;

/**
 * 聚合根基类
 * <p>
 * 聚合根是 DDD 中的核心概念，代表一组相关对象的集合，
 * 对外提供统一的业务操作入口
 */
public abstract class BaseAggregate extends BaseEntity {

    private static final long serialVersionUID = 1L;
}

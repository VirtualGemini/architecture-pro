package com.architecturepro.domain.model;

/**
 * 值对象基类
 * <p>
 * 值对象没有唯一标识，通过属性值来判断相等性
 */
public abstract class BaseValueObject extends BaseEntity {

    private static final long serialVersionUID = 1L;
}

package com.velox.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 通用 Mapper 基类
 * <p>
 * 所有数据库 Mapper 接口继承此类，统一 MyBatis Plus 的基础 CRUD 操作
 *
 * @param <T> 实体类型
 */
public interface BaseMapperExt<T> extends BaseMapper<T> {
}

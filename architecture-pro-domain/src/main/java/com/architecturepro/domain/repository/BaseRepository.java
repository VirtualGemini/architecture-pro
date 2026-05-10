package com.architecturepro.domain.repository;

/**
 * 仓储接口基类
 * <p>
 * DDD 中仓储属于领域层接口，由基础设施层实现
 *
 * @param <T>  聚合根类型
 * @param <ID> 主键类型
 */
public interface BaseRepository<T, ID> {

    /**
     * 根据ID查找
     */
    T findById(ID id);

    /**
     * 保存
     */
    T save(T entity);

    /**
     * 根据ID删除
     */
    void deleteById(ID id);
}

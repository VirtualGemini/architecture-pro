package com.velox.infrastructure.persistence;

import com.velox.domain.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapperExt<Role> {
}

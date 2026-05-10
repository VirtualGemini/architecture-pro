package com.architecturepro.infrastructure.persistence;

import com.architecturepro.domain.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapperExt<Role> {
}

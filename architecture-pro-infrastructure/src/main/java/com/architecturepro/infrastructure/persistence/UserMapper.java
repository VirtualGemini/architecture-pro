package com.architecturepro.infrastructure.persistence;

import com.architecturepro.domain.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapperExt<User> {
}

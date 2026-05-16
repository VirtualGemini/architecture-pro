package com.velox.infrastructure.persistence;

import com.velox.domain.model.Profile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfileMapper extends BaseMapperExt<Profile> {
}

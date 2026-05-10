package com.architecturepro.infrastructure.persistence;

import com.architecturepro.domain.model.Profile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfileMapper extends BaseMapperExt<Profile> {
}

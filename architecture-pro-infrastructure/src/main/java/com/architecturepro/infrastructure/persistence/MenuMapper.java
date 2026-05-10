package com.architecturepro.infrastructure.persistence;

import com.architecturepro.domain.model.Menu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapperExt<Menu> {
}

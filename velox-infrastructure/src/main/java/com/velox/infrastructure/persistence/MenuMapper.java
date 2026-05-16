package com.velox.infrastructure.persistence;

import com.velox.domain.model.Menu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapperExt<Menu> {
}

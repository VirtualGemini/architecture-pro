package com.architecturepro.infrastructure.persistence;

import com.architecturepro.domain.model.FileConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileConfigMapper extends BaseMapperExt<FileConfig> {

    default FileConfig selectByMaster() {
        return selectOne(new LambdaQueryWrapper<FileConfig>()
                .eq(FileConfig::getMaster, true));
    }

    default List<FileConfig> selectListByMaster(Boolean master) {
        return selectList(new LambdaQueryWrapper<FileConfig>()
                .eq(FileConfig::getMaster, master));
    }

    default void updateNoneMaster() {
        update(new LambdaUpdateWrapper<FileConfig>()
                .set(FileConfig::getMaster, false)
                .eq(FileConfig::getMaster, true));
    }
}

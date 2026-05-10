package com.architecturepro.infrastructure.persistence;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IdSequenceMapper {

    @Select("SELECT current_value FROM sys_id_sequence WHERE biz_code = #{bizCode} FOR UPDATE")
    Integer selectCurrentValueForUpdate(@Param("bizCode") String bizCode);

    @Insert("INSERT INTO sys_id_sequence (biz_code, current_value) VALUES (#{bizCode}, #{currentValue})")
    int insert(@Param("bizCode") String bizCode, @Param("currentValue") int currentValue);

    @Update("UPDATE sys_id_sequence SET current_value = #{currentValue}, update_time = CURRENT_TIMESTAMP WHERE biz_code = #{bizCode}")
    int update(@Param("bizCode") String bizCode, @Param("currentValue") int currentValue);
}

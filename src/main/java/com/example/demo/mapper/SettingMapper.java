package com.example.demo.mapper;

import com.example.demo.entity.resourcePath;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SettingMapper {

    List<resourcePath> selectAllPath();

    @Select("select * from resource_path where path = #{path}")
    resourcePath isHavePath(String path);

    @Insert("insert into resource_path (path, type) values (#{path}, #{type}) ")
    boolean insertPath(resourcePath path);
}

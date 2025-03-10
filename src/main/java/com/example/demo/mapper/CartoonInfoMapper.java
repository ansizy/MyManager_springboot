package com.example.demo.mapper;

import com.example.demo.entity.CartoonInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CartoonInfoMapper {

    List<CartoonInfo> selectPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    boolean insertCartoonInfoList(List<CartoonInfo> infoList);

    @Select("select count(*) from cartoon_info")
    Integer countCartoon();

    @Select("select path from cartoon_info where auto_id = #{id}")
    String selectPathByAutoId(Integer id);

    @Select("select * from cartoon_info where name = #{name}")
    CartoonInfo selectCartoonByName(String name);
}

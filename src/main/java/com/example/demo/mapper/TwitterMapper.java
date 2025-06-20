package com.example.demo.mapper;

import com.example.demo.entity.Tweet;
import com.example.demo.entity.TwitterInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 主要是处理 twitter_info 和 tweet 两张表
 */
public interface TwitterMapper {


    boolean insertTweetList(List<Tweet> list);

    boolean insertTwitterInfoList(List<TwitterInfo> twitterInfoList);

    @Select("select * from twitter_info")
    List<TwitterInfo> selectTwitterPageAll();

    @Select("select * from tweet where user_name = #{userName} ORDER BY tweet_date DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<Tweet> selectTweetListByUserName(@Param("userName") String userName,
                                          @Param("offset") int offset,
                                          @Param("pageSize") int pageSize);

    @Select("select path from twitter_info where user_name = #{userName}")
    String selectPathByUserName(@Param("userName") String userName);

    @Select("select count(*) from twitter_info")
    int countTwitterNumber();

    // 这里path未用上
    @Select("select user_name from twitter_info")
    List<String> selectAllTwitterUserNameByPath(String path);

    @Delete("delete * from tweet where user_name = #{userName}")
    int deleteAllTweetByUsername(String userName);

    int updateTwitterInfo(TwitterInfo twitterInfo);

    @Select("select count(*) from tweet where user_name = #{userName}")
    int countTweetNumberByUsername(String userName);

    @Select("select * from twitter_info where user_name like CONCAT('%', #{keyword}, '%') or display_name like CONCAT('%', #{keyword}, '%')")
    List<TwitterInfo> selectTwitterByKeyword(String keyword);
}

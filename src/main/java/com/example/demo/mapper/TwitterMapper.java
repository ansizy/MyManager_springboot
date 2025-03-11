package com.example.demo.mapper;

import com.example.demo.entity.Tweet;

import java.util.List;

/**
 * 主要是处理 twitter_info 和 tweet 两张表
 */
public interface TwitterMapper {


    boolean insertTweetList(List<Tweet> list);
}

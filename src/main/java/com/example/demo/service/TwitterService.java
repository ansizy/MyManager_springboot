package com.example.demo.service;

import com.example.demo.entity.Tweet;
import com.example.demo.entity.TwitterInfo;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.TwitterMapper;
import com.example.demo.utils.TwitterUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class TwitterService {

    @Resource
    private TwitterMapper twitterMapper;

    /**
     *
     * 通过磁盘地址添加tweet
     * @param path 磁盘地址
     * @return 是否添加成功
     */
    public boolean addTweetListBypath(String path) {
        try {
            List<Tweet> list = TwitterUtil.parseCsvToTweets(Paths.get(path));
            return twitterMapper.insertTweetList(list);
        } catch (IOException e) {
            throw new CustomException("500", "解析csv文件失败");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addTwitterInfoListBypath(String path) {
        return false;
    }

    public int getTotalTweets() {
        return 0;
    }

    public int getTotalTwitterInfos() {
        return 0;
    }

    public List<Tweet> getTweetListByUserName(String userName) {
        return null;
    }
}

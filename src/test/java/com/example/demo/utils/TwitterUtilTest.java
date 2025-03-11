package com.example.demo.utils;

import com.example.demo.entity.Tweet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


class TwitterUtilTest {
    @Test
    void getTweetCsv() throws Exception {
        String path = "F:\\新建文件夹\\t\\_UWUChenbaby\\_UWUChenbaby-2025-01-01_09-50-46.csv";
        List<Tweet> tweetList = TwitterUtil.parseCsvToTweets(Paths.get(path));
        for (Tweet tweet : tweetList) {
            System.out.println(tweet);
        }
    }

}

package com.example.demo.utils;

import com.example.demo.entity.Tweet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 用于处理Twitter相关的工具类
 */
public class TwitterUtil {

    /**
     *
     * @param csvFilePath 文件路径
     * @return 推文列表
     * @throws IOException io错误
     */
    public static List<Tweet> parseCsvToTweets(Path csvFilePath) throws IOException {
        List<Tweet> tweets = new ArrayList<>();

        // 手动跳过前三行
        try (BufferedReader reader = Files.newBufferedReader(csvFilePath)) {
            for (int i = 0; i < 3; i++) {
                reader.readLine(); // 跳过前三行
            }

            // 从第四行开始解析
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader() // 使用第四行作为标题
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            CSVParser csvParser = csvFormat.parse(reader);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

            for (CSVRecord record : csvParser) {
                Tweet tweet = new Tweet();

                // 解析日期字段
                String tweetDateStr = record.get("Tweet Date");
                try {
                    Date tweetDate = dateFormat.parse(tweetDateStr);
                    tweet.setTweetDate(tweetDate);
                } catch (ParseException e) {
                    System.err.println("解析日期失败: " + tweetDateStr);
                    continue; // 跳过该行
                }

                // 设置其他字段
                tweet.setDisplayName(record.get("Display Name"));
                tweet.setUserName(record.get("User Name"));
                tweet.setTweetUrl(record.get("Tweet URL"));
                tweet.setMediaType(record.get("Media Type"));
                tweet.setMediaUrl(record.get("Media URL"));
                tweet.setSavedFilename(record.get("Saved Filename"));
                tweet.setTweetContent(record.get("Tweet Content"));

                // 转换数值字段
                tweet.setFavoriteCount(convertToInteger(record.get("Favorite Count")));
                tweet.setRetweetCount(convertToInteger(record.get("Retweet Count")));
                tweet.setReplyCount(convertToInteger(record.get("Reply Count")));

                tweets.add(tweet);
            }
        }

        return tweets;
    }

    private static Integer convertToInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null; // 处理无效数值
        }
    }
}

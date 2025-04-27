package com.example.demo.service;

import cn.hutool.core.io.file.FileNameUtil;
import com.example.demo.entity.Tweet;
import com.example.demo.entity.TwitterInfo;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.TwitterMapper;
import com.example.demo.utils.ImageUtil;
import com.example.demo.utils.PathUtil;
import com.example.demo.utils.TwitterUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
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
            List<Tweet> list = getTweetListBypath(path);
            return twitterMapper.insertTweetList(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addTwitterInfoListAndTweetListBypath(String resourcePath) {
        List<String> stringList = PathUtil.getAllPath2Cartoon(resourcePath);
        List<TwitterInfo> twitterInfoList = new ArrayList<>();
        for (String path : stringList) {
            File dir = new File(path);
            String userName = FileNameUtil.mainName(path);
            if (dir.isDirectory() && dir.exists()) {
                File[] files = dir.listFiles();
                int imageNumber = 0;
                int videoNumber = 0;
                int number = 0;
                String displayName = null;
                assert files != null;
                for (File file : files) {
                    if (FileNameUtil.extName(file).equals("csv")) {
                        // 添加tweet列表
                        List<Tweet> tweetList = getTweetListBypath(file.getPath());
                        if(!tweetList.isEmpty()){
                            twitterMapper.insertTweetList(tweetList);
                            number = tweetList.size();
                            displayName = tweetList.get(0).getDisplayName();
                        }
                    }
                    else if (FileNameUtil.extName(file).equals("png") || FileNameUtil.extName(file).equals("jpg")) {
                        imageNumber++;
                    }
                    else if (FileNameUtil.extName(file).equals("mp4")) {
                        videoNumber++;
                    }
                    else {
                        continue;
                    }
                }
                // 组装twitterInfo实体类
                TwitterInfo twitterInfo = new TwitterInfo();
                twitterInfo.setUserName(userName);
                twitterInfo.setNumber(number);
                twitterInfo.setImageNumber(imageNumber);
                twitterInfo.setVideoNumber(videoNumber);
                twitterInfo.setPath(path);
                twitterInfo.setJoinTime(new Date());
                twitterInfo.setDisplayName(displayName);
                twitterInfoList.add(twitterInfo);
            }

        }

        return twitterMapper.insertTwitterInfoList(twitterInfoList);
    }

    public int getTotalTweets() {
        return 0;
    }

    public int getTotalTwitterInfos() {
        return 0;
    }

    public String getBasePathByUserName(String userName) {
        return twitterMapper.selectPathByUserName(userName);
    }

    public List<Tweet> getTweetListByUserName(String userName,
                                              Integer page,
                                              Integer pageSize) throws IOException {
        int offset = (page - 1) * pageSize;
        // 获取文件夹磁盘地址
        String path = twitterMapper.selectPathByUserName(userName);

        List<Tweet> tweetList = twitterMapper.selectTweetListByUserName("@" + userName, offset, pageSize);
        // 加载图片
        for (Tweet tweet : tweetList) {
            if (tweet.getMediaType().equals("Image")) {
                String imagePath = path + "\\" + tweet.getSavedFilename();
                String image = ImageUtil.getImageByPath(imagePath);
                tweet.setMediaUrl(image);
            }
            else if (tweet.getMediaType().equals("Video")) {
                String videoPath = "/" + userName + "/" + tweet.getSavedFilename();
                tweet.setMediaUrl(videoPath);
            }
        }
        return tweetList;
    }

    private List<Tweet> getTweetListBypath(String path) {
        try {
            return TwitterUtil.parseCsvToTweets(Paths.get(path));
        } catch (IOException e) {
            throw new CustomException("500", "解析csv文件失败");
        }
    }

    public List<TwitterInfo> getTwitterPageAll() {
        return twitterMapper.selectTwitterPageAll();
    }
}

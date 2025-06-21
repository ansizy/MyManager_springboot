package com.example.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Tweet {
    private Integer autoId;
    private String userName;
    private String displayName;
    private String tweetUrl;
    private String mediaUrl;
    private Date tweetDate;
    private String tweetContent;
    private String mediaType;
    private String savedFilename;
    private Integer favoriteCount;
    private Integer retweetCount;
    private Integer replyCount;
    private Integer isLike;
}

package com.example.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 对于数据表 av_info
 */
@Data
public class av_info {

    private Integer autoId;
    private String avId;
    private String avTitle;
    private String avPlot;
    private String avLocal;
    private String avPoster;
    private String avFanart;
    private Date publishTime;
    private Date joinTime;
    private Date updateTime;
    private boolean isLike;
    private boolean isDownload;
    private Integer runTime;
}

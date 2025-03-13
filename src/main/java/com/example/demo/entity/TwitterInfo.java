package com.example.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TwitterInfo {

    private Integer autoId;
    private String userName;
    private String displayName;
    private String path;
    private Integer number;
    private Date joinTime;
    private String description;
    private Integer imageNumber;
    private Integer videoNumber;
}

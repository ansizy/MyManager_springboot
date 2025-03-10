package com.example.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CartoonInfo {

    private Integer autoId;
    // 标题
    private String name;
    // 磁盘地址
    private String path;
    // 漫画页数
    private Integer pageNumber;

    private Date joinTime;
}

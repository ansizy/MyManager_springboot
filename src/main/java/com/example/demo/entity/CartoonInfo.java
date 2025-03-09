package com.example.demo.entity;

import lombok.Data;

@Data
public class CartoonInfo {

    private Integer id;
    // 标题
    private String name;
    // 磁盘地址
    private String path;
    // 漫画页数
    private Integer pageNumber;
}

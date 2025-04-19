package com.example.demo.entity;

import lombok.Data;

@Data
public class PageInfo {
    private String userName;
    private Integer page;
    private Integer pageSize;
}

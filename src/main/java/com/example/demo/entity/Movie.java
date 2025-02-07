package com.example.demo.entity;

import lombok.Data;

import java.util.List;

/**
 * 用于将movie.xml文件转换为java对象
 */
@Data
public class Movie {
    private String title;
    private String originalTitle;
    private double rating;
    private String plot;
    private int runtime;
    private String mpaa;
    private String premiered;
    private String studio;
    private String country;
    private String director;
    private List<UniqueId> uniqueIds;
    private List<String> genres;
    private List<String> tags;
    private List<Actor> actors;
}

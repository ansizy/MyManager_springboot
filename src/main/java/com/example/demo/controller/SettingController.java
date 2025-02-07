package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.resourcePath;
import com.example.demo.service.MovieService;
import com.example.demo.service.SettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/setting")
public class SettingController {

    @Resource
    private SettingService settingService;

    @Resource
    private MovieService movieService;

    @GetMapping("/getAllPath")
    public Result getAllPath() {
        List<resourcePath> res = settingService.getAllPath();
        return Result.success(res);
    }

    @PostMapping("/add")
    public Result addNewPath(@RequestBody resourcePath resourcePath) {
        // 判断数据库是否已经有该地址
        boolean havePath = settingService.isHavePath(resourcePath.getPath());
        if (havePath) {
            return Result.error("500","该地址已存在!");
        }

        boolean res = false;
        if (resourcePath.getType().equals("电影")) {
            res = movieService.addList(resourcePath.getPath());
        }
        if(res){
            // path 写入数据库
            settingService.addPath(resourcePath);
            return Result.success();
        }
        else {
            return Result.error();
        }

    }

    @PostMapping("/potplayer/add")
    public Result addPotplayerPath(@RequestBody resourcePath resourcePath) {
        // 判断数据库是否已经有该地址
        boolean havePath = settingService.isHavePath(resourcePath.getPath());
        if (havePath) {
            return Result.error("500","该地址已存在!");
        }

        if (resourcePath.getType().equals("potplayer")) {
            settingService.addPath(resourcePath);
            return Result.success();
        }
        else {
            return Result.error("500", "地址类型错误");
        }

    }

    @PostMapping("/update")
    public Result updateContent(@RequestBody resourcePath resourcePath) {
        if(resourcePath.getPath().isEmpty() || resourcePath.getType().isEmpty()){
            return Result.error("500","地址或类型为空");
        }
        else{
            Integer i = movieService.updateContent(resourcePath);
            if(i == 0){
                return Result.error("500", "没有新增电影");
            }else {
                return Result.success(i);
            }

        }
    }

    @PostMapping("/delete")
    public Result deletePath(@RequestBody resourcePath resourcePath) {
        if(resourcePath.getPath().isEmpty() || resourcePath.getType().isEmpty()){
            return Result.error("500","地址或类型为空");
        }
        else{
            Integer i = movieService.deleteContent(resourcePath);
            return Result.success(i);
        }
    }
}

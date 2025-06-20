package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.resourcePath;
import com.example.demo.service.CartoonService;
import com.example.demo.service.MovieService;
import com.example.demo.service.SettingService;
import com.example.demo.service.TwitterService;
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

    @Resource
    private CartoonService cartoonService;

    @Resource
    private TwitterService twitterService;

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
        else if(resourcePath.getType().equals("漫画")) {
            res = cartoonService.addList(resourcePath.getPath());
        }
        else if (resourcePath.getType().equals("推特")) {
            res = twitterService.addTwitterInfoListAndTweetListBypath(resourcePath.getPath());
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
            Integer i = 0;
            if(resourcePath.getType().equals("电影")){
                i = movieService.updateContent(resourcePath);
            }
            else if (resourcePath.getType().equals("漫画")){
                i = cartoonService.updateCartoonContent(resourcePath);
            }
            else if (resourcePath.getType().equals("推特")){
                // 与电影 和 漫画 不同，微博的逻辑不同，微博的更新单独实现，根据每个博主的地址进行 "/twitter/update"
                // 这里是有新增博主的更新，对原有的博主内容不更新
                i = twitterService.updateNewTwitterContent(resourcePath);
                // i = 0;
            }
            else{
                i = 0;
            }

            if(i == 0){
                return Result.error("500", "没有新增电影");
            }else {
                return Result.success(i);
            }

        }
    }

    /**
     * TODO 删除逻辑还没有实现，只是空有架子
     * @param resourcePath 地址
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public Result deletePath(@RequestBody resourcePath resourcePath) {
        if(resourcePath.getPath().isEmpty() || resourcePath.getType().isEmpty()){
            return Result.error("500","地址或类型为空");
        }
        else{
            Integer i = 0;
            if(resourcePath.getType().equals("电影")){
                i = movieService.deleteContent(resourcePath);
            }
            else if (resourcePath.getType().equals("漫画")){
                i = 0;
            }
            else if (resourcePath.getType().equals("微博")){
                i = 0;
            }
            else{
                i = 0;
            }
            return Result.success(i);
        }
    }


    @PostMapping("/twitter/update")
    public Result updateTwitterContent(@RequestBody resourcePath resourcePath) {
        if(resourcePath.getPath().isEmpty() || resourcePath.getType().isEmpty()){
            return Result.error("500","地址或类型为空");
        }
        else {
            Integer i = 0;
            if (resourcePath.getType().equals("推特")) {
                i = cartoonService.updateCartoonContent(resourcePath);
            }

            if(i == 0){
                return Result.error("500", "没有新增内容");
            }else {
                return Result.success(i);
            }
        }
    }
}

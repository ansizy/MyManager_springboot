package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.TwitterInfo;
import com.example.demo.service.TwitterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/twitter")
public class TwitterController {

    @Resource
    private TwitterService twitterService;

    @GetMapping("/page/all")
    public Result getTwitterPageAll() {
        return Result.success(twitterService.getTwitterPageAll());
    }

    @PostMapping("/getTweet")
    public Result getTwitterTweet(@RequestBody TwitterInfo twitterInfo) {
        return Result.success(twitterService.getTweetListByUserName("@" + twitterInfo.getUserName()));
    }
}

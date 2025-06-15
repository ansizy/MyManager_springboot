package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.PageInfo;
import com.example.demo.service.TwitterService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Result getTwitterTweet(@RequestBody PageInfo pageInfo) throws IOException {
        return Result.success(twitterService.getTweetListByUserName(
                pageInfo.getUserName(),
                pageInfo.getPage(),
                pageInfo.getPageSize()));
    }

    @GetMapping("/total")
    public Result getTwitterTotal() {
        return Result.success(twitterService.getTotalTwitter());

    }

}

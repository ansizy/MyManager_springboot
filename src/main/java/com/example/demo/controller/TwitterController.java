package com.example.demo.controller;

import cn.hutool.poi.excel.cell.CellSetter;
import com.example.demo.common.Result;
import com.example.demo.entity.PageInfo;
import com.example.demo.entity.TwitterInfo;
import com.example.demo.service.TwitterService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

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

    /**
     *  获取喜欢的tweet
     * @param pageInfo 分页
     * @return tweet
     * @throws IOException io异常
     */
    @PostMapping("/like/getTweet")
    public Result getLikeTweet(@RequestBody PageInfo pageInfo) throws IOException {
        return Result.success(twitterService.getLikeTweetList(
                pageInfo.getUserName(),
                pageInfo.getPage(),
                pageInfo.getPageSize()));
    }


    /**
     * 获取全部博主数量
     * @return 博主数量
     */
    @GetMapping("/total")
    public Result getTwitterTotal() {
        return Result.success(twitterService.getTotalTwitter());

    }

    /**
     * 获取单个博主的推文数量
     * @param userName
     * @return 单个博主的推文数量 @PathVariable String id
     */
    @GetMapping("/user/total")
    public Result getTwitterInfo(@RequestParam String userName) {
        if (userName == null || userName.isEmpty()) {
            return Result.error("500", "用户信息为空");
        }
        int res = twitterService.getTotalByUsername(userName);
        return Result.success(res);
    }

    /**
     * 更新单个博主的信息
     * @param twitterInfo 需要更新的信息
     * @return 是否更新成功
     */
    @PostMapping("/update")
    public Result updateTwitter(@RequestBody TwitterInfo twitterInfo) {
        // 获取path
        String path = twitterService.getBasePathByUserName(twitterInfo.getUserName());
        if (path == null) {
            return Result.error();
        }
        else {
            // 交给service
            int n = twitterService.updateExitTwitterContent(twitterInfo.getUserName(), path);
            return Result.success(n);
        }
    }

    @GetMapping("/search")
    public Result searchTwitter(@RequestParam String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return Result.error();
        }
        List<TwitterInfo> res =  twitterService.searchTwitterByKeyword(keyword);
        return Result.success(res);
    }

    // TODO 喜欢列表

    /**
     * 添加到喜欢
     * @param autoId 唯一id
     * @return 是否添加成功
     */
    @GetMapping("/like/add/tweet")
    public Result addLikeTweet(@RequestParam int autoId) {
        if (autoId <= 0) {
            return Result.error();
        }
        if (twitterService.addLikeTweetByAutoId(autoId))
            return Result.success();
        else
            return Result.error();
    }

    /**
     * 去除喜欢
     * @param autoId 唯一id
     * @return 是否移除成功
     */
    @GetMapping("/like/remove/tweet")
    public Result removeLikeTweet(@RequestParam int autoId) {
        if (autoId <= 0) {
            return Result.error();
        }
        if (twitterService.removeLikeTweetByAutoId(autoId))
            return Result.success();
        else
            return Result.error();
    }



}

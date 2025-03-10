package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.CartoonInfo;
import com.example.demo.service.CartoonService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 漫画接口
 */
@RestController
@RequestMapping("/cartoon")
public class CartoonController {

    @Resource
    private CartoonService cartoonService;

    /**
     * 获取分页数据
     * @return 分页数据
     */
    @GetMapping("/page")
    public Result getPage(@RequestParam int currentPage, @RequestParam int pageSize, @RequestParam int orderBy) {
        List<CartoonInfo> resList = null;
        try {
            resList = cartoonService.getPage(currentPage, pageSize, orderBy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(resList);
    }

    /**
     *
     * @return 总漫画数
     */
    @GetMapping("/total")
    public Result getTotal() {
        int total = cartoonService.getTotal();
        return Result.success(total);
    }

    /**
     * 通过 自增id获取漫画
     * @param id 自增id
     * @return 整本漫画
     */
    @GetMapping("/search/{id}")
    public Result getSearch(@PathVariable String id) {

        return Result.success();
    }

    /**
     * 通过漫画名搜索（模糊搜索）
     * @param name 漫画名
     * @return 整本漫画
     */
    @PostMapping("/searchLikeName")
    public Result getSearchName(@RequestParam String name) {
        return Result.success();
    }

}

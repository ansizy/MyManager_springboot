package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.PageInfo;
import com.example.demo.entity.TwitterInfo;
import com.example.demo.service.TwitterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     * TODO 视频流处理
     * @param filename 文件名称
     * @param response 回答
     * @param range 时间范围
     */
    @GetMapping("/stream/video/{filename}")
    public void streamVideo(
            @PathVariable String filename,
            HttpServletResponse response,
            @RequestHeader(value = "Range", required = false) String range) {
        String path = "F:\\新建文件夹\\t\\_UWUChenbaby\\" + filename;

        Path videoPath = Paths.get(path);
        File videoFile = videoPath.toFile();

        try (RandomAccessFile raf = new RandomAccessFile(videoFile, "r")) {
            long length = raf.length();
            long start = 0;
            long end = length - 1;

            if (range != null) {
                String[] ranges = range.substring("bytes=".length()).split("-");
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1) end = Long.parseLong(ranges[1]);
            }

            response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
            response.setHeader("Content-Type", "video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(end - start + 1));
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);

            raf.seek(start);
            byte[] buffer = new byte[1024 * 8];
            long remaining = end - start + 1;

            while (remaining > 0) {
                int read = raf.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                response.getOutputStream().write(buffer, 0, read);
                remaining -= read;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

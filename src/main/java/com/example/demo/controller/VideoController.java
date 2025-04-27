package com.example.demo.controller;

import com.example.demo.service.TwitterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;

@RestController
public class VideoController {
    //private static final String VIDEO_DIR = "H:\\落梁识别\\";
    private static final int BUFFER_SIZE = 64 * 1024; // 64KB缓冲区
    private static final String CONTENT_TYPE = "video/mp4; codecs=\"avc1.42E01E, mp4a.40.2\"";

    @Resource
    private TwitterService twitterService;

    @GetMapping("/stream/video/{username}/{filename}")
    public void streamVideo(
            @PathVariable String username,
            @PathVariable String filename,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            // 1. 构建安全路径
            // Path basePath = Paths.get(VIDEO_DIR).toAbsolutePath().normalize();
            // 根据userneam获取路径
            username = username.substring(1, username.length());
            System.out.println("username: " + username);
            String VIDEO_DIR = twitterService.getBasePathByUserName(username);
            System.out.println("VIDEO_DIR: " + VIDEO_DIR);
            Path basePath = Paths.get(VIDEO_DIR).toAbsolutePath().normalize();
            Path videoPath = basePath.resolve(filename).normalize();
            System.out.println("videoPath: " + videoPath.toAbsolutePath());
            // 2. 安全检查
            if (!videoPath.startsWith(basePath)) {
                sendError(response, HttpStatus.FORBIDDEN, "访问被拒绝");
                return;
            }

            File videoFile = videoPath.toFile();
            if (!videoFile.exists()) {
                sendError(response, HttpStatus.NOT_FOUND, "文件不存在");
                return;
            }
            if (!videoFile.canRead()) {
                sendError(response, HttpStatus.FORBIDDEN, "文件不可读");
                return;
            }

            // 3. 准备流参数
            long fileLength = videoFile.length();
            String rangeHeader = request.getHeader("Range");
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Accept-Ranges", "bytes");

            // 4. 处理完整文件请求
            if (rangeHeader == null) {
                response.setHeader("Content-Length", String.valueOf(fileLength));
                response.setStatus(HttpStatus.OK.value());
                try (InputStream is = new BufferedInputStream(new FileInputStream(videoFile))) {
                    copyStream(is, response.getOutputStream());
                }
                return;
            }

            // 5. 处理范围请求
            String[] ranges = rangeHeader.substring(6).split("-");
            long start = parseLongSafe(ranges[0], 0);
            long end = ranges.length > 1 ? parseLongSafe(ranges[1], fileLength - 1) : fileLength - 1;
            end = Math.min(end, fileLength - 1);

            // 6. 验证范围有效性
            if (start < 0 || end >= fileLength || start > end) {
                response.setHeader("Content-Range", "bytes */" + fileLength);
                sendError(response, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "无效范围请求");
                return;
            }

            long contentLength = end - start + 1;
            response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
            response.setHeader("Content-Length", String.valueOf(contentLength));
            response.setHeader("Content-Range",
                    String.format("bytes %d-%d/%d", start, end, fileLength));

            // 7. 分块传输
            try (RandomAccessFile raf = new RandomAccessFile(videoFile, "r")) {
                raf.seek(start);
                long remaining = contentLength;
                byte[] buffer = new byte[BUFFER_SIZE];

                OutputStream os = response.getOutputStream();
                while (remaining > 0) {
                    int read = raf.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                    if (read == -1) break;
                    os.write(buffer, 0, read);
                    remaining -= read;
                }
            }

        } catch (Exception e) {
            handleStreamingException(e, response);
        }
    }

    // 辅助方法：安全转换long
    private long parseLongSafe(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // 辅助方法：流复制
    private void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
    }

    // 统一错误处理
    private void sendError(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        response.sendError(status.value(), message);
        logError("视频流错误: " + message + " [" + status + "]");
    }

    // 异常处理
    private void handleStreamingException(Exception e, HttpServletResponse response) {
        try {
            logError("流传输异常: " + e.getMessage());
            if (!response.isCommitted()) {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "视频流处理失败");
            }
        } catch (IOException ex) {
            logError("发送错误响应失败: " + ex.getMessage());
        }
    }

    // 日志记录（可替换为Logger）
    private void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
}

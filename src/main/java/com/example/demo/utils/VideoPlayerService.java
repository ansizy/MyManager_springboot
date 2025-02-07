package com.example.demo.utils;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;

@Service
public class VideoPlayerService {

    public void play(String potplayerPath, String moviePath) {
        // 检查PotPlayer路径是否存在且是一个文件
        File potplayerFile = new File(potplayerPath);
        if (!potplayerFile.isFile()) {
            throw new IllegalArgumentException("PotPlayer路径不正确或不可执行: " + potplayerPath);
        }

        try {
            // 构建ProcessBuilder并设置命令参数
            ProcessBuilder processBuilder = new ProcessBuilder(potplayerPath, moviePath);

            // 启动进程
            Process process = processBuilder.start();

            // 处理输入流和错误流，避免进程阻塞
            handleStreams(process);

        } catch (IOException e) {
            throw new RuntimeException("启动PotPlayer失败: " + e.getMessage(), e);
        }
    }

    // 处理进程的输入流和错误流
    private void handleStreams(Process process) {
        // 消费标准输入流
        new Thread(() -> consumeStream(process.getInputStream())).start();

        // 消费错误流
        new Thread(() -> consumeStream(process.getErrorStream())).start();
    }

    // 读取并丢弃流内容，防止阻塞
    private void consumeStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (reader.readLine() != null) {
                // 忽略输出内容，或记录到日志
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

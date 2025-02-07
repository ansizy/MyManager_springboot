package com.example.demo.utils;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

/**
 * 图片工具类
 */
public class ImageUtil {

    /**
     * 根据磁盘地址返回图片base64编码
     * @param imagePath 图片磁盘地址
     * @return 图片base64编码
     */
    public static String getImageByPath(String imagePath) throws IOException {
        File imageFile = new File(imagePath); // 创建文件对象
        if (imageFile.exists()) {
            // 读取图片文件并转换为字节数组
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

            // 获取文件扩展名并根据扩展名确定 MIME 类型
            String mimeType = getMimeType(imagePath);

            // 将字节数组编码为 Base64 字符串，并返回符合格式的字符串
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return "data:" + mimeType + ";base64," + base64Image;
        } else {
            throw new IOException("Image not found at " + imagePath);
        }
    }


    // 根据文件路径获取 MIME 类型（支持 jpeg、png 等格式）
    private static String getMimeType(String imagePath) {
        if (imagePath.toLowerCase().endsWith(".jpg") || imagePath.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (imagePath.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (imagePath.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else {
            return "image/jpeg";  // 默认返回 jpeg 类型
        }
    }
}





















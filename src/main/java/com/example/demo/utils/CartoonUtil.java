package com.example.demo.utils;

import com.example.demo.entity.CartoonInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 漫画工具类
 */
public class CartoonUtil {

    /**
     * 根据磁盘地址获取漫画列表
     * @param resourcePath 磁盘地址
     * @return 漫画列表
     */
    public static List<CartoonInfo> getCartoonInfoListByPath(String resourcePath) {
        List<String> allPath = PathUtil.getAllPath2Cartoon(resourcePath);
        List<CartoonInfo> cartoonInfoList = new ArrayList<>();
        for (String path : allPath) {
            File dir = new File(path);
            // 如果该路径是一个文件夹，并且存在
            if (dir.isDirectory() && dir.exists()) {
                // 漫画名称
                String[] s = path.split("\\\\");
                String fileName = s[s.length - 1];
                // 漫画页数
                int pageNumber = 0;
                // 获取漫画信息
                File[] files = dir.listFiles();
                if(files != null) {
                    pageNumber = files.length;
                }
                // 组装实体类
                CartoonInfo cartoonInfo = new CartoonInfo();
                cartoonInfo.setName(fileName);
                cartoonInfo.setPageNumber(pageNumber);
                cartoonInfo.setPath(path);
                cartoonInfoList.add(cartoonInfo);
            }
        }
        return cartoonInfoList;
    }

    /**
     *
     * 根据地址获取封面 Poster : 封面
     * @param resourcePath 本子磁盘地址
     * @return 封面
     */
    public static String getPosterByPath(String resourcePath) throws IOException {
        String path = resourcePath + "\\00001.webp";
        String poster = ImageUtil.getImageByPath(path);
        return poster;
    }
}

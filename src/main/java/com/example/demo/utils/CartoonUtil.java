package com.example.demo.utils;

import com.example.demo.entity.CartoonInfo;
import com.example.demo.exception.CustomException;

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
            }else {
                throw new CustomException("400","源目录不存在");
            }
        }
        return cartoonInfoList;
    }

    /**
     *
     * 根据地址获取封面
     * @param resourcePath 本子磁盘地址
     * @return 封面
     */
    public static String getPosterByPath(String resourcePath) throws IOException {
        File dir = new File(resourcePath);
        File[] files = dir.listFiles();
        if(files != null && files.length > 0) {
            return getPosterByPathAndPathName(resourcePath, files[0].getName());
        }
        return null;
    }

    /**
     * 磁盘地址 文件夹中的所有文件 转变为http连接
     * G:\本子\精品\危險!_性徒會 [ねいさん] 絕 あぶないっ!_ 性徒會 (コミック ジエス 04) [中國翻譯] [DL版]\00001.webp
     * http://localhost:8080/cartoon/{autoId}/00001.webp
     *
     * @param resourcePath
     * @return
     */
    public static List<String> path2Http(String resourcePath, Integer autoId) {
        File dir = new File(resourcePath);
        String baseUrl = "http://localhost:8080/cartoon/getPoster/" + autoId.toString() + "/";
        // 返回结果
        List<String> httpList = new ArrayList<String>();
        File[] files = dir.listFiles();
        if(files != null) {
            for (File file : files) {
                String temp = baseUrl + file.getName();
                httpList.add(temp);
            }
        }
        return httpList;
    }

    public static String getPosterByPathAndPathName(String path, String pathName) throws IOException {

        File dir = new File(path);
        if (dir.isDirectory() && dir.exists()) {
            String temp = path + File.separator + pathName;
            String poster = ImageUtil.getImageByPath(temp);
            return poster;
        }
        else {
            throw new CustomException("400","源目录不存在");
        }
    }
}

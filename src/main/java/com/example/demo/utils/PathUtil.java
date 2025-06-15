package com.example.demo.utils;

import com.example.demo.exception.CustomException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 磁盘地址处理工具类
 */
public class PathUtil {
    /**
     *  获取最低一层的所有地址
     * @param path "G:\\本子"
     * @return G:\\本子\\每个本子文件夹
     */
    public static List<String> getAllPath2Cartoon(String path) {
        File dir = new File(path);
        // 返回结果
        List<String> pathList = new ArrayList<String>();

        if(dir.isDirectory() && dir.exists()) {
            File[] files = dir.listFiles();
            for(File file : files) {
                String secondFileName = file.getName();
                String secondFilePath = path + "\\" + secondFileName;
                pathList.add(secondFilePath);
            }
        }
        else {
            throw new CustomException("400","源目录不存在");
        }
        return pathList;
    }

    /**
     *
     * @param path 地址 "G:\\本子"
     * @return filename "a", "b", "b"
     */
    public static List<String> getAllFileNameFromPath(String path) {
        File dir = new File(path);
        // 返回结果
        List<String> res = new ArrayList<String>();
        if(dir.isDirectory() && dir.exists()) {
            File[] files = dir.listFiles();
            for(File file : files) {
                res.add(file.getName());
            }
        }
        else {
            throw new CustomException("400","源目录不存在");
        }
        return res;
    }

}

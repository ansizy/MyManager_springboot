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
     * @param path "G:\\课件\\动漫\\杂\\1\\#整理完成"
     * @return G:\课件\动漫\杂\1\#整理完成\#未知女优\[MIMK-103] 不擅长社交的寡妇雪女和被诅咒的戒指已售出两万多份！人气怪物春药漫画的真人版！
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
}

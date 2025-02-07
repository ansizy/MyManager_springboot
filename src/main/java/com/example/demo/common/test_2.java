package com.example.demo.common;

import java.io.File;
import java.util.ArrayList;

public class test_2 {
    public static void main(String[] args) {
        String path = "G:\\课件\\动漫\\杂\\1\\#整理完成";
        File dir = new File(path);
        ArrayList<String> pathList = new ArrayList<String>();

        if(dir.isDirectory() && dir.exists()) {
            File[] files = dir.listFiles();
            for(File file : files) {
                String secondFileName = file.getName();
                String secondFilePath = path + "\\" + secondFileName;
                File thirdDir = new File(secondFilePath);
                File[] thirdFiles = thirdDir.listFiles();
                for(File thirdFile : thirdFiles) {
                    String thirdFileName = thirdFile.getName();
                    String thirdFilePath = secondFilePath + "\\" + thirdFileName;
                    pathList.add(thirdFilePath);
                }
            }
        }
        else {
            System.out.println("源目录不存在");
        }

        for(String s : pathList) {
            File dir1 = new File(s);

            if(dir1.isDirectory() && dir1.exists()) {
                File[] files = dir1.listFiles();
                if(files != null) {
                    for(File file : files) {
                        System.out.println(file.getName());
                        // 对不同的文件进行不同的处理 .mp4
                        String name = file.getName();
                        int dotIndex = name.lastIndexOf(".");
                        if(dotIndex > 0 && dotIndex < name.length() - 1) {
                            // 文件后缀名
                            String fileExtension = name.substring(dotIndex + 1);
                            // 文件前缀名
                            String front = name.substring(0, dotIndex);
                            System.out.println("文件名: " + front + "\t后缀名: " + fileExtension);

                        }

                    }
                }
                else {
                    System.out.println("文件夹为空或无法访问。");
                }
            }
            else {
                System.out.println("路径失效");
            }
        }

    }
}

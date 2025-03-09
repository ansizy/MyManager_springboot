package com.example.demo.utils;

import com.example.demo.entity.CartoonInfo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartoonUtilTest {
    @Test
    void getCartoon() {
        String path = "G:\\本子";
        File dir = new File(path);
        int a = dir.listFiles().length;
        System.out.println("第一目录个数: " + a);

        List<CartoonInfo> infoList = CartoonUtil.getCartoonInfoListByPath(path);
        System.out.println(infoList.size());
        for (CartoonInfo info : infoList) {
            System.out.println(info.getName() + info.getPageNumber());
        }
    }
    @Test
    void getPosterByPath() {
        String path = "G:\\本子\\[Banana重新汉化]  [苍山哲]ドハマりママ～息子の亲友とヤリたい牝～ (ANGEL 俱乐部 2022年3月号) [中国翻译] [DL版]";
        String s = null;
        try {
            s = CartoonUtil.getPosterByPath(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(s);
    }

}

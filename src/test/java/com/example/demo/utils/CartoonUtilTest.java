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
    void getHttp() {
        String path = "G:\\本子\\精品\\[よもだよも] 高學曆(インテリ)人妻雨宮透子準教授(せんせい)の發情";
        Integer autoId = 1;
        List<String> list = CartoonUtil.path2Http(path, autoId);
        for (String s : list) {
            System.out.println(s);
        }
    }

}

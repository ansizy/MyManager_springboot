package com.example.demo.service;

import com.example.demo.entity.CartoonInfo;
import com.example.demo.mapper.CartoonInfoMapper;
import com.example.demo.utils.CartoonUtil;
import com.example.demo.utils.ImageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class CartoonService {

    @Resource
    private CartoonInfoMapper cartoonInfoMapper;
    public List<CartoonInfo> getPage(int currentPage, int pageSize, int orderBy) throws IOException {

        int offset = (currentPage - 1) * pageSize;
        List<CartoonInfo> res;
        // TODO orderBy 排序 目前为按加入数据库时间降序排列
        res = cartoonInfoMapper.selectPage(offset, pageSize);
        for (CartoonInfo cartoon : res) {
            // 封面处理 将原本的 漫画path 转换为 封面的http
            String poster = CartoonUtil.getPosterByPath(cartoon.getPath());
            cartoon.setPath(poster);
        }
        return res;
    }

    public boolean addList(String path) {
        List<CartoonInfo> infoList = CartoonUtil.getCartoonInfoListByPath(path);
        return cartoonInfoMapper.insertCartoonInfoList(infoList);
    }

    public int getTotal() {
        int res = cartoonInfoMapper.countCartoon();
        return res;
    }

    public String getPoster(Integer id, String posterName) {
        String path = cartoonInfoMapper.selectPathByAutoId(id);
        try {
            String poster = CartoonUtil.getPosterByPathAndPathName(path, posterName);
            return poster;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

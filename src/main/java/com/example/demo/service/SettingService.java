package com.example.demo.service;

import com.example.demo.entity.resourcePath;
import com.example.demo.mapper.SettingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SettingService {
    @Resource
    private SettingMapper settingMapper;

    public List<resourcePath> getAllPath() {
        return settingMapper.selectAllPath();
    }

    public boolean isHavePath(String path) {
        resourcePath res = settingMapper.isHavePath(path);
        return res != null;
    }

    public boolean addPath(resourcePath path) {
        return settingMapper.insertPath(path);
    }
}

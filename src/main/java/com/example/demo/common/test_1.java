package com.example.demo.common;

import java.io.File;
import java.util.List;

import com.example.demo.entity.Actor;
import com.example.demo.entity.Movie;
import com.example.demo.entity.UniqueId;
import com.example.demo.service.MovieService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * path 磁盘地址
 * dir 文件夹
 */
public class test_1 {
    public static void main(String[] args) {
        String path = "G:\\课件\\动漫\\杂\\1\\#整理完成\\つばさ舞\\[SSIS-549] 出差时，竟然和自己鄙视的中年性骚扰老板同住一个房间……G罩杯新员工突然感觉不自在，性交一直持续到早上。\\movie.nfo";
        // 解析nfo文件
        try {
            // 读取 XML 文件
            File xmlFile = new File(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);

            // 解析 XML 文件到 Movie 类
            Movie movie = new Movie();

            Element root = document.getRootElement();

            // 解析简单字段
            movie.setTitle(root.elementText("title"));
            movie.setOriginalTitle(root.elementText("originaltitle"));
            movie.setRating(Double.parseDouble(root.elementText("rating")));
            movie.setPlot(root.elementText("plot"));
            movie.setRuntime(Integer.parseInt(root.elementText("runtime")));
            movie.setMpaa(root.elementText("mpaa"));
            movie.setPremiered(root.elementText("premiered"));
            movie.setStudio(root.elementText("studio"));
            movie.setCountry(root.elementText("country"));
            movie.setDirector(root.elementText("director"));

            // 解析 <uniqueid> 节点
            List<UniqueId> uniqueIds = new ArrayList<>();
            for (Element uniqueIdElement : root.elements("uniqueid")) {
                UniqueId uniqueId = new UniqueId();
                uniqueId.setType(uniqueIdElement.attributeValue("type"));
                uniqueId.setDefault("true".equals(uniqueIdElement.attributeValue("default")));
                uniqueId.setValue(uniqueIdElement.getText());
                uniqueIds.add(uniqueId);
            }
            movie.setUniqueIds(uniqueIds);

            // 解析 <genre> 节点
            List<String> genres = new ArrayList<>();
            for (Element genreElement : root.elements("genre")) {
                genres.add(genreElement.getText());
            }
            movie.setGenres(genres);

            // 解析 <tag> 节点
            List<String> tags = new ArrayList<>();
            for (Element tagElement : root.elements("tag")) {
                tags.add(tagElement.getText());
            }
            movie.setTags(tags);

            // 解析 <actor> 节点
            List<Actor> actors = new ArrayList<>();
            for (Element actorElement : root.elements("actor")) {
                Actor actor = new Actor();
                actor.setName(actorElement.elementText("name"));
                actor.setThumb(actorElement.elementText("thumb"));
                actors.add(actor);
            }
            movie.setActors(actors);

            // 打印解析结果
            System.out.println(movie);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

}

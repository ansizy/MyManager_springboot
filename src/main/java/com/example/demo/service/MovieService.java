package com.example.demo.service;

import cn.hutool.core.date.DateUtil;
import com.example.demo.entity.*;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.MovieInfoMapper;
import com.example.demo.utils.ImageUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

/**
 * 处理movie.nfo
 */
@Service
public class MovieService {

    @Resource
    private MovieInfoMapper movieInfoMapper;

    // 获取av信息列表
    public List<av_info> selectAll() {
        // 从数据库拿数据
        return movieInfoMapper.selectAll();
    }

    // 分页查询
    public List<avInfoSimple> getMoviePage(int currentPage, int pageSize, int orderBy) {
        int offset = (currentPage - 1) * pageSize;
        // 0 : 出版时间; 1 : 加入时间  均为降序排列
        List<avInfoSimple> res;
        if(orderBy == 1){
            // 1 : 加入时间
            res = movieInfoMapper.selectPage(offset, pageSize);
        }
        else if(orderBy == 0){
            // 0 : 出版时间
            res = movieInfoMapper.selectPageOrderByPublishTime(offset, pageSize);
        }
        else {
            res = null;
        }
        // 将avPoster地址转换为image base64 编码
        for (avInfoSimple avInfoSimple : res) {
            try {
                String image = ImageUtil.getImageByPath(avInfoSimple.getAvPoster());
                avInfoSimple.setAvPoster(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    // 获取电影总数
    public Integer getMovieCount() {
        return movieInfoMapper.selectCount();
    }

    // 写avInfo到数据库
    public boolean addList(String path) {
        // 得到av_info列表
        List<String> allPath = getAllPath(path);
        List<av_info> avInfos = movieInfoHandler(allPath);
        // 写入数据库
        return movieInfoMapper.insertList(avInfos);
    }

    /**
     * 根据番号查找
     * @param fanhao
     */
    public av_info getByfanhao(String fanhao) {
        return movieInfoMapper.selectById(fanhao);
    }

    public String getFanartByFanhao(String fanhao) {
        String path = movieInfoMapper.selectFanartPathByFanhao(fanhao);
        if(path != null) {
            try {
                String image = ImageUtil.getImageByPath(path);
                return image;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public List<String> getActorsByFanhao(String fanaho) {
        List<String> list = movieInfoMapper.selectActorsByFanhao(fanaho);
        if(list != null) {
            return list;
        }
        return null;
    }

    public List<String> getGenreByFanhao(String fanhao) {
        List<String> list = movieInfoMapper.selectGenreByFanhao(fanhao);
        if(list != null) {
            return list;
        }
        return null;
    }

    public List<String> getTagByFanhao(String fanhao) {
        List<String> list = movieInfoMapper.selectTagByFanhao(fanhao);
        if(list != null) {
            return list;
        }
        return null;
    }

    public List<avInfoSimple> searchLike(String search) {
        List<avInfoSimple> res = movieInfoMapper.selectLike("%" + search.toUpperCase() + "%");
        // 将avPoster地址转换为image base64 编码
        for (avInfoSimple avInfoSimple : res) {
            try {
                String image = ImageUtil.getImageByPath(avInfoSimple.getAvPoster());
                avInfoSimple.setAvPoster(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    public List<avInfoSimple> getAllLikeMovie(int currentPage, int pageSize) {
        int offset = (currentPage - 1) * pageSize;
        List<avInfoSimple> res = movieInfoMapper.selectLikeMoviePage(offset, pageSize);
        // 将avPoster地址转换为image base64 编码
        for (avInfoSimple avInfoSimple : res) {
            try {
                String image = ImageUtil.getImageByPath(avInfoSimple.getAvPoster());
                avInfoSimple.setAvPoster(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    public int getLikeMovieTotal() {
        return movieInfoMapper.countLikeMovies();
    }

    public boolean addLikeMovie(String fanhao) {
        return movieInfoMapper.updateMovieLike(fanhao);
    }

    public boolean removeLikeMovie(String fanhao) {
        return movieInfoMapper.updateMovieLikeRemove(fanhao);
    }

    public String getPotPlayerPath() {
        return movieInfoMapper.selectPotPlayerPath();
    }

    public void openMovieFolder(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            try {
                // 尝试使用 Desktop
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file.getParentFile());
            } catch (HeadlessException e) {
                // 如果无头环境，回退到命令行工具
                try {
                    openFolderUsingCommand(file);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("文件不存在: " + filePath);
        }
    }

    public int updateContent(resourcePath resourcePath) {
        if(resourcePath.getType().equals("电影")) {
            List<String> allPath = getAllPath(resourcePath.getPath());
            List<av_info> avInfos = movieInfoHandler2Update(allPath);
            if(avInfos.isEmpty()) {
                return 0;
            }
            movieInfoMapper.insertList(avInfos);
            return avInfos.size();
        }
        else
            return 0;

    }

    public int deleteContent(resourcePath resourcePath) {
            return 0;
    }

    public Integer getActorMovieTotal(String name) {
        return movieInfoMapper.countActorMoviesTotal(name);
    }

    public List<avInfoSimple> getActorMoviePage(int currentPage, int pageSize, String name, int orderBy) {
        int offset = (currentPage - 1) * pageSize;
        // 获得番号列表
        // 0 : 出版时间; 1 : 加入时间  均为降序排列
        List<avInfoSimple> res;
        if(orderBy == 1){
            // 1 : 加入时间
            res = movieInfoMapper.selectActorPage(offset, pageSize, name);
        }
        else if(orderBy == 0){
            // 0 : 出版时间
            res = movieInfoMapper.selectActorPageOrderByPublishTime(offset, pageSize, name);
        }
        else {
            res = null;
        }
        // 将avPoster地址转换为image base64 编码
        for (avInfoSimple avInfoSimple : res) {
            try {
                String image = ImageUtil.getImageByPath(avInfoSimple.getAvPoster());
                avInfoSimple.setAvPoster(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }


    // 处理目录所有文件 and movie --> av_info
    private List<av_info> movieInfoHandler(List<String> allPath) {
        List<av_info> resList = new ArrayList<>();
        for(String s : allPath) {
            File dir1 = new File(s);

            if(dir1.isDirectory() && dir1.exists()) {
                File[] files = dir1.listFiles();
                if(files != null) {
                    Movie movie = new Movie();
                    String posterPath = "", fanartPath = "", localPath = "";

                    for(File file : files) {
                        // 对四个文件进行不同的处理，写进数据库
                        String name = file.getName();
                        if(name.equals("movie.nfo")){
                            movie = nfo2Movie(file);
                        }
                        else if(name.equals("poster.jpg")){
                            posterPath = s + "\\poster.jpg";
                        }
                        else if(name.equals("fanart.jpg")){
                            fanartPath = s + "\\fanart.jpg";
                        }
                        else{
                            localPath = s + "\\" + file.getName();
                        }
                    }
                    // 组装av_info
                    av_info avInfo = movie2avInfo(movie, localPath, posterPath, fanartPath);
                    resList.add(avInfo);

                    String fanhao = movie.getUniqueIds().get(0).getValue();
                    // 获取 avActor list
                    List<avActor> avActors = movie2avActor(fanhao, movie.getActors());
                    if(!avActors.isEmpty()) {
                        movieInfoMapper.insertAvActorList(avActors);
                    }

                    // 获取 avGenre list
                    List<avGenre> avGenreList = movie2avGenre(fanhao, movie.getGenres());
                    movieInfoMapper.insertAvGenreList(avGenreList);
                    // 获取 avTag list
                    List<avTag> avTagList = movie2avTag(fanhao, movie.getTags());
                    movieInfoMapper.insertAvTagList(avTagList);

                }
                else {
                    System.out.println("文件夹为空或无法访问。");
                }
            }
            else {
                System.out.println("路径失效");
            }
        }
        return resList;
    }

    private List<av_info> movieInfoHandler2Update(List<String> allPath) {
        List<av_info> resList = new ArrayList<>();
        for(String s : allPath) {
            File dir1 = new File(s);

            if(dir1.isDirectory() && dir1.exists()) {
                File[] files = dir1.listFiles();
                if(files != null) {
                    Movie movie = new Movie();
                    String posterPath = "", fanartPath = "", localPath = "";

                    for(File file : files) {
                        // 对四个文件进行不同的处理，写进数据库
                        String name = file.getName();
                        if(name.equals("movie.nfo")){
                            movie = nfo2Movie(file);
                        }
                        else if(name.equals("poster.jpg")){
                            posterPath = s + "\\poster.jpg";
                        }
                        else if(name.equals("fanart.jpg")){
                            fanartPath = s + "\\fanart.jpg";
                        }
                        else{
                            localPath = s + "\\" + file.getName();
                        }
                    }

                    String fanhao = movie.getUniqueIds().get(0).getValue();
                    av_info dataBaseRes = movieInfoMapper.selectById(fanhao);
                    if(dataBaseRes == null) {
                        // 组装av_info
                        av_info avInfo = movie2avInfo(movie, localPath, posterPath, fanartPath);
                        resList.add(avInfo);
                        // 获取 avActor list
                        List<avActor> avActors = movie2avActor(fanhao, movie.getActors());
                        if(!avActors.isEmpty()) {
                            movieInfoMapper.insertAvActorList(avActors);
                        }
                        // 获取 avGenre list
                        List<avGenre> avGenreList = movie2avGenre(fanhao, movie.getGenres());
                        movieInfoMapper.insertAvGenreList(avGenreList);
                        // 获取 avTag list
                        List<avTag> avTagList = movie2avTag(fanhao, movie.getTags());
                        movieInfoMapper.insertAvTagList(avTagList);
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
        return resList;
    }

    private List<Movie> getAllMovie(List<String> allPath) {
        List<Movie> resList = new ArrayList<>();
        for(String s : allPath) {
            File dir1 = new File(s);
            if(dir1.isDirectory() && dir1.exists()) {
                File[] files = dir1.listFiles();
                if(files != null) {
                    Movie movie = new Movie();

                    for(File file : files) {
                        String name = file.getName();
                        if(name.equals("movie.nfo")){
                            movie = nfo2Movie(file);
                            resList.add(movie);
                        }
                    }
                }
            }
        }
        return resList;
    }

    /**
     *  获取所有地址
     * @param path "G:\\课件\\动漫\\杂\\1\\#整理完成"
     * @return G:\课件\动漫\杂\1\#整理完成\#未知女优\[MIMK-103] 不擅长社交的寡妇雪女和被诅咒的戒指已售出两万多份！人气怪物春药漫画的真人版！
     */
    private List<String> getAllPath(String path) {
        File dir = new File(path);
        // 返回结果
        List<String> pathList = new ArrayList<String>();

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
            throw new CustomException("400","源目录不存在");
        }
        return pathList;
    }

    private Movie nfo2Movie(File xmlFile) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);

            // 解析 XML 文件到 Movie 类
            Movie movie = new Movie();

            Element root = document.getRootElement();

            // 解析简单字段
            movie.setTitle(root.elementText("title"));
            movie.setOriginalTitle(root.elementText("originaltitle"));
            String rating = root.elementText("rating");
            if(rating != null && !rating.isEmpty()) {
                try {
                    movie.setRating(Double.parseDouble(root.elementText("rating")));
                }catch (NumberFormatException e){
                    System.err.println("无效的评分值: " + rating);
                    movie.setRating(0.0);
                }
            }
            else{
                System.err.println("评分值为空或不存在: " + xmlFile.getAbsoluteFile());
            }
            // movie.setRating(Double.parseDouble(root.elementText("rating")));
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
            return movie;

            // 打印解析结果
            // System.out.println(movie);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private av_info movie2avInfo(Movie movie, String localPath, String posterPath, String fanartPath){
        av_info avInfo = new av_info();

        avInfo.setAvId(movie.getUniqueIds().get(0).getValue());
        avInfo.setAvTitle(movie.getTitle());
        avInfo.setAvPlot(movie.getPlot());
        avInfo.setAvLocal(localPath);
        avInfo.setAvPoster(posterPath);
        avInfo.setAvFanart(fanartPath);
        avInfo.setPublishTime(DateUtil.parse(movie.getPremiered()));
        avInfo.setJoinTime(new Date());
        avInfo.setUpdateTime(new Date());
        avInfo.setLike(false);
        avInfo.setDownload(true);
        avInfo.setRunTime(movie.getRuntime());
        return avInfo;
    }

    private List<avActor> movie2avActor(String fanhao, List<Actor> actors) {

        List<avActor> avActors = new ArrayList<>();
        for (Actor actor : actors) {
            avActor item = new avActor();
            item.setAvFanhao(fanhao);
            item.setActorName(actor.getName());
            avActors.add(item);
        }
        return avActors;
    }

    private List<avGenre> movie2avGenre(String fanhao, List<String> genres) {
        List<avGenre> res = new ArrayList<>();

        for(String s : genres){
            avGenre item = new avGenre();
            item.setAvFanhao(fanhao);
            item.setAvGenre(s);
            res.add(item);
        }
        return res;
    }

    private List<avTag> movie2avTag(String fanhao, List<String> tags) {
        List<avTag> res = new ArrayList<>();

        for(String s : tags){
            avTag item = new avTag();
            item.setAvFanhao(fanhao);
            item.setAvTag(s);
            res.add(item);
        }
        return res;
    }

    private void openFolderUsingCommand(File file) throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder;

        if (os.contains("win")) {
            // Windows
            processBuilder = new ProcessBuilder("explorer.exe", "/select,", file.getAbsolutePath());
        } else if (os.contains("mac")) {
            // macOS
            processBuilder = new ProcessBuilder("open", "-R", file.getAbsolutePath());
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            // Linux/Unix
            processBuilder = new ProcessBuilder("xdg-open", file.getParent());
        } else {
            throw new UnsupportedOperationException("不支持的操作系统: " + os);
        }

        // 启动进程
        Process process = processBuilder.start();
        process.waitFor(); // 等待命令执行完成
    }

}

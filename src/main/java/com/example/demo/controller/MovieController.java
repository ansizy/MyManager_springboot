package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Movie;
import com.example.demo.entity.avInfoSimple;
import com.example.demo.entity.av_info;
import com.example.demo.service.MovieService;
import com.example.demo.utils.VideoPlayerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Resource
    private MovieService movieService;

    @Resource
    private VideoPlayerService videoPlayerService;


    @GetMapping("/page")
    public Result getMoviePage(@RequestParam int currentPage, @RequestParam int pageSize, @RequestParam int orderBy) {
        List<avInfoSimple> res = movieService.getMoviePage(currentPage, pageSize, orderBy);
        return Result.success(res);
    }

    /**
     * 获取电影总数
     * @return total
     */
    @GetMapping("/total")
    public Result getMovieTotal() {
        return Result.success(movieService.getMovieCount());
    }

    @GetMapping("/test")
    public Result test() {
        List<av_info> avInfos = movieService.selectAll();
        return Result.success(avInfos);
    }

    /**
     * url 形式: http://localhost:8080/movie/selcet/id
     * @param id
     * @return
     */
    @GetMapping("/seclet/{id}")
    public Result getMovieById(@PathVariable String id) {
        return Result.error();
    }

    /**
     * url 形式 : http://localhost:8080/movie/selcet?id = 1&name=1
     */
    @GetMapping("/seclet2")
    public Result getMovieById2(@RequestParam String id, @RequestParam String name) {
        return Result.error();
    }

    /**
     *根据番号获取电影信息
     * @param fanhao
     * @return
     */
    @GetMapping("/getbyfanhao")
    public Result getMovieByFanhao(@RequestParam String fanhao) {
        av_info avInfo = movieService.getByfanhao(fanhao);
        return Result.success(avInfo);
    }

    @GetMapping("/getFanartByFanhao")
    public Result getMovieFanartByFanhao(@RequestParam String fanhao) {
        String res = movieService.getFanartByFanhao(fanhao);
        if(res != null)
            return Result.success(res);
        else
            return Result.error();
    }

    @GetMapping("/getActorsByFanhao")
    public Result getMovieActorsByFanhao(@RequestParam String fanhao) {
        List<String> names = movieService.getActorsByFanhao(fanhao);
        return Result.success(names);
    }

    @GetMapping("/getGenreByFanhao")
    public Result getMovieGenreByFanhao(@RequestParam String fanhao) {
        List<String> genres = movieService.getGenreByFanhao(fanhao);
        return Result.success(genres);
    }

    @GetMapping("/getTagByFanhao")
    public Result getMovieTagByFanhao(@RequestParam String fanhao) {
        List<String> tags = movieService.getTagByFanhao(fanhao);
        return Result.success(tags);
    }

    /**
     * 模糊查询
     * @param search 关键词
     * @return movie列表
     */
    @GetMapping("/search")
    public Result search(@RequestParam String search) {
        List<avInfoSimple> res = movieService.searchLike(search);
        return Result.success(res);
    }

    /**
     * 获取喜欢列表 同时分页
     * @return 喜欢列表
     */
    @GetMapping("/page/like")
    public Result getLikeMovie(@RequestParam int currentPage, @RequestParam int pageSize) {
        List<avInfoSimple> res = movieService.getAllLikeMovie(currentPage, pageSize);
        return Result.success(res);
    }

    @GetMapping("/total/like")
    public Result getLikeTotal() {
        return Result.success(movieService.getLikeMovieTotal());
    }

    @GetMapping("/page/actor")
    public Result getActorMoviePage(@RequestParam int currentPage,
                                    @RequestParam int pageSize,
                                    @RequestParam String name,
                                    @RequestParam int orderBy) {
        List<avInfoSimple> res = movieService.getActorMoviePage(currentPage, pageSize, name, orderBy);
        return Result.success(res);
    }

    @GetMapping("/total/actor")
    public Result getActorTotal(@RequestParam String name) {
        return Result.success(movieService.getActorMovieTotal(name));
    }

    @GetMapping("/addLike")
    public Result addNewLike(@RequestParam String fanhao) {
        boolean res = movieService.addLikeMovie(fanhao);
        if(res)
            return Result.success();
        return Result.error();
    }

    @GetMapping("/removeLike")
    public Result removeLikeMovie(@RequestParam String fanhao) {
        boolean res = movieService.removeLikeMovie(fanhao);
        if(res)
            return Result.success();
        return Result.error();
    }

    @PostMapping("/playMovie")
    public Result playMovie(@RequestBody av_info avInfo) {
        String potplayerPath = movieService.getPotPlayerPath();
        videoPlayerService.play(potplayerPath, avInfo.getAvLocal());
        return Result.success();
    }

    @PostMapping("/openFolder")
    public Result openFile(@RequestBody av_info avInfo) {
        movieService.openMovieFolder(avInfo.getAvLocal());
        return Result.success();
    }
}

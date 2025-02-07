package com.example.demo.mapper;

import com.example.demo.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MovieInfoMapper {
    List<av_info> selectAll();


    @Select("select * from av_info where av_id = #{fanhao}")
    av_info selectById(String fanhao);

    boolean insertList(List<av_info> avInfos);

    boolean insertAvActorList(List<avActor> avActors);

    boolean insertAvGenreList(List<avGenre> avGenreList);

    boolean insertAvTagList(List<avTag> avTagList);

    List<avInfoSimple> selectPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select count(*) from av_info")
    Integer selectCount();

    @Select("select av_fanart from av_info where av_id = #{fanaho}")
    String selectFanartPathByFanhao(String fanhao);

    @Select("select actor_name from av_actor where av_fanhao = #{fanhao}")
    List<String> selectActorsByFanhao(String fanhao);

    @Select("select av_genre from av_genre where av_fanhao = #{fanhao}")
    List<String> selectGenreByFanhao(String fanhao);

    @Select("select av_tag from av_tag where av_fanhao = #{fanhao}")
    List<String> selectTagByFanhao(String fanhao);

    List<avInfoSimple> selectLike(String search);

    List<avInfoSimple> selectPageOrderByPublishTime(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<avInfoSimple> selectLikeMoviePage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select count(*) from av_info where is_like = 1")
    int countLikeMovies();

    @Update("update av_info set is_like = 1 where av_id = #{fanhao}")
    boolean updateMovieLike(String fanhao);

    @Update("update av_info set is_like = 0 where av_id = #{fanhao}")
    boolean updateMovieLikeRemove(String fanhao);

    @Select("select path from resource_path where type = 'potplayer'")
    String selectPotPlayerPath();

    @Select("select count(*) from av_actor where actor_name = #{name}")
    Integer countActorMoviesTotal(String name);


    List<avInfoSimple> selectActorPage(@Param("offset") int offset,
                                       @Param("pageSize") int pageSize,
                                       @Param("name") String name);

    List<avInfoSimple> selectActorPageOrderByPublishTime(@Param("offset") int offset,
                                                         @Param("pageSize") int pageSize,
                                                         @Param("name") String name);

}

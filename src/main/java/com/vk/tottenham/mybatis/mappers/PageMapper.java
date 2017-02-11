package com.vk.tottenham.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.vk.tottenham.core.model.Page;

public interface PageMapper {
    @Select("SELECT season as season, competition as competition, "
            + "pageType as pageType, pageLink as pageLink "
            + "FROM Pages "
            + "WHERE season=#{season} "
            + "AND competition=#{competition} "
            + "AND pageType=#{pageType}")
    Page findById(Page page);

    @Select("SELECT season as season, competition as competition, "
            + "pageType as pageType, pageLink as pageLink "
            + "FROM Pages "
            + "ORDER BY season ASC, competition ASC, pageType ASC")
    List<Page> findAll();

    @Insert("INSERT INTO Pages(season, competition, pageType, pageLink) VALUES"
            + "(#{season},#{competition},#{pageType}, #{pageLink})")
    void save(Page page);

    @Select("SELECT season as season, competition as competition, "
            + "pageType as pageType, pageLink as pageLink "
            + "FROM Pages "
            + "WHERE season=#{season} "
            + "AND competition=#{competition}")
    List<Page> findByYearAndCompetition(Page page);

    @Select("SELECT season as season, competition as competition, "
            + "pageType as pageType, pageLink as pageLink "
            + "FROM Pages "
            + "WHERE pageLink=#{pageLink}")
    Page findByPageLink(String pageLink);

    @Insert("UPDATE Pages set pageLink=#{pageLink} "
            + "WHERE season=#{season} AND pageType=#{pageType} "
            + "AND competition=#{competition} ")
    void update(Page page);
}
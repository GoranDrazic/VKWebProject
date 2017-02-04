package com.vk.tottenham.mybatis.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.vk.tottenham.core.model.Article;

public interface ArticleMapper {
    @Select("SELECT id as id, guid as guid, "
            + "title as title, link as link, "
            + "originalPubDate as originalPubDate, description as description, "
            + "content as content, thumbnail as thumbnail "
            + "FROM Articles "
            + "WHERE id=#{id}")
    Article findById(long id);

    @Insert("INSERT INTO Articles(id, guid, title, link,"
            + "originalPubDate, description, content, thumbnail) VALUES"
            + "(#{id},#{guid},#{title}, #{link}, #{originalPubDate},"
            + "#{description}, #{content}, #{thumbnail})")
    @Options(useGeneratedKeys = true, keyProperty = "id", flushCache = true, keyColumn = "id")
    void save(Article news);
}

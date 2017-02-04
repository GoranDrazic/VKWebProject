package com.vk.tottenham.mybatis.mappers;

import org.apache.ibatis.annotations.Select;

public interface DictionaryMapper {
    @Select("SELECT russianNom as russianNom "
            + "FROM Dictionary "
            + "WHERE english=#{term}")
    String translateInRussianNom(String term);
}

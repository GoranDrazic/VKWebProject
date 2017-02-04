package com.vk.tottenham.mybatis.mappers;

import org.apache.ibatis.annotations.Select;

public interface TeamMapper {
    @Select("SELECT zimbioName as zimbioName "
            + "FROM Teams "
            + "WHERE fapiName=#{fapiName}")
    public String getZimbioNameByFapiName(String fapiName);

    @Select("SELECT russianNameNom as russianNameNom "
            + "FROM Teams "
            + "WHERE fapiName=#{fapiName}")
    public String getRussianTeamNameInNominativeByFapiName(String fapiName);

    @Select("SELECT standingsIcon as standingsIcon "
            + "FROM Teams "
            + "WHERE zimbioName=#{zimbioName}")
    public String getStandingsIconByZimbioName(String zimbioName);

    @Select("SELECT russianNameNom as russianNameNom "
            + "FROM Teams "
            + "WHERE zimbioName=#{zimbioName}")
    public String getRussianTeamNameInNominativeByZimbioName(String zimbioName);

    @Select("SELECT standingsIcon as standingsIcon "
            + "FROM Teams "
            + "WHERE fapiName=#{fapiName}")
    String getStandingsIconByFapiName(String fapiName);
}

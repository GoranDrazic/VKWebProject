package com.vk.tottenham.mybatis.service;

public interface TeamService {
    String getZimbioNameByFapiName(String fapiName);
    String getRussianTeamNameInNominativeByFapiName(String fapiName);
    String getStandingsIconByZimbioName(String zimbioName);
    String getRussianTeamNameInNominativeByZimbioName(String zimbioName);
    String getStandingsIconByFapiName(String fapiName);
}

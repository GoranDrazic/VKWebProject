package com.vk.tottenham.mybatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vk.tottenham.mybatis.mappers.TeamMapper;
import com.vk.tottenham.mybatis.service.TeamService;

@Service("teamService")
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper mapper;

    @Override
    public String getZimbioNameByFapiName(String fapiName) {
        return mapper.getZimbioNameByFapiName(fapiName);
    }

    @Override
    public String getRussianTeamNameInNominativeByFapiName(String fapiName) {
        return mapper.getRussianTeamNameInNominativeByFapiName(fapiName);
    }

    @Override
    public String getStandingsIconByZimbioName(String zimbioName) {
        return mapper.getStandingsIconByZimbioName(zimbioName);
    }

    @Override
    public String getRussianTeamNameInNominativeByZimbioName(String zimbioName) {
        return mapper.getRussianTeamNameInNominativeByZimbioName(zimbioName);
    }

    @Override
    public String getStandingsIconByFapiName(String fapiName) {
        return mapper.getStandingsIconByFapiName(fapiName);
    }
}

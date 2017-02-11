package com.vk.tottenham.mybatis.service;

import java.util.List;

import com.vk.tottenham.core.model.Page;
import com.vk.tottenham.footballapi.model.CompetitionName;

public interface PageService {
    Page findById(Page page);

    List<Page> findAll();

    void save(Page page);

    List<Page> findByYearAndCompetition(String seasonFull,
            CompetitionName competition);

    Page findByPageLink(String pageLink);

    void update(Page page);
}

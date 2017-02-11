package com.vk.tottenham.mybatis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vk.tottenham.core.model.Page;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.mybatis.mappers.PageMapper;
import com.vk.tottenham.mybatis.service.PageService;

@Service("pageService")
public class PageServiceImpl implements PageService {

    @Autowired
    private PageMapper pageMapper;

    @Override
    public Page findById(Page page) {
        return pageMapper.findById(page);
    }

    @Override
    public List<Page> findAll() {
        return pageMapper.findAll();
    }

    @Override
    public void save(Page page) {
        pageMapper.save(page);
    }

    @Override
    public List<Page> findByYearAndCompetition(String seasonFull,
            CompetitionName competition) {
        Page page = new Page();
        page.setSeason(seasonFull);
        page.setCompetition(competition);
        return pageMapper.findByYearAndCompetition(page);
    }

    @Override
    public Page findByPageLink(String pageLink) {
        return pageMapper.findByPageLink(pageLink);
    }

    @Override
    public void update(Page page) {
        pageMapper.update(page);
    }
}

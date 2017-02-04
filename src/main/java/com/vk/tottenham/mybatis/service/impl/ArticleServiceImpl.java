package com.vk.tottenham.mybatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.mybatis.mappers.ArticleMapper;
import com.vk.tottenham.mybatis.service.ArticleService;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Article findById(long id) {
        return articleMapper.findById(id);
    }

    @Override
    public void save(Article news) {
        articleMapper.save(news);
    }
}

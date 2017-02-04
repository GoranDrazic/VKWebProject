package com.vk.tottenham.mybatis.service;

import com.vk.tottenham.core.model.Article;

public interface ArticleService {

    Article findById(long id);

    void save(Article news);

}

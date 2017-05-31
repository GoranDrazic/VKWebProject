package com.vk.tottenham.scheduler;

import static com.vk.tottenham.utils.NewsFeedLoader.FAPL_FEED_URL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.core.model.NewsSource;

public class FaplNewsLoader extends NewsLoaderBase {

    Pattern pattern = Pattern.compile("http://fapl.ru/posts/(.*?)/");

    @Override
    protected NewsSource getNewsSource() {
        return NewsSource.FAPL;
    }

    @Override
    protected String getArticleId(Article article) {
        Matcher matcher = pattern.matcher(article.getLink());
        matcher.find();

        return matcher.group(1).trim();
    }

    @Override
    protected String getChatMessagePrefix() {
        return "Новые статьи c FAPL.ru";
    }

    @Override
    protected String getFeedURL() {
        return FAPL_FEED_URL;
    }

    
    @Override
    protected String getArticle(Article news) {
        return "«" + news.getTitle() + "». Источник: " + news.getLink()+ ".";
    }
}

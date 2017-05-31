package com.vk.tottenham.scheduler;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.core.model.NewsSource;
import com.vk.tottenham.core.model.ResourceType;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.NewsFeedLoader;

public abstract class NewsLoaderBase extends SchedulerBase {
    private static final Logger LOGGER = Logger.getLogger(NewsLoaderBase.class);

    @Autowired
    @Qualifier("resourceService")
    protected ResourceService articleService;

    @Autowired
    protected NewsFeedLoader feedLoader;

    @Override
    public void execute() {
        StringBuilder articlePart = new StringBuilder();
        for (String postedArticle : getPostedArticles()) {
            articlePart.append("• ").append(postedArticle).append("\n");
        }
        if (articlePart.length() > 0) {
            vkGateway.sendChatMessage(getChatMessagePrefix() + ": \n" + articlePart, getChatId());
        }
    }

    protected List<String> getPostedArticles() {
        List<Article> feedNews = feedLoader.loadNewsFeed(getFeedURL());
        List<String> postedArticles = new LinkedList<>();
        for (Article news : feedNews) {
            try {
                String articleId = getArticleId(news);
                if (!articleService.exists(ResourceType.NEWS.value(), getNewsSource().value(), articleId)) {
                    LOGGER.info("Loading new article: " + news.getTitle());
                    postedArticles.add(getArticle(news));

                    articleService.save(ResourceType.NEWS.value(), getNewsSource().value(), getArticleId(news));
                } else {
                    LOGGER.info("Ignoring: " + news.getTitle());
                }
            } catch (Exception e) {
                //TODO: temporarily swallow this dude
            }
        }
        return postedArticles;
    }

    protected abstract String getArticle(Article news);

    protected abstract String getArticleId(Article article);

    protected abstract String getFeedURL();

    protected abstract String getChatMessagePrefix();

    protected abstract NewsSource getNewsSource();
}

package com.vk.tottenham.scheduler;

import static com.vk.tottenham.utils.NewsFeedLoader.FAPL_FEED_URL;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.core.model.Resource;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.NewsFeedLoader;

public class FaplNewsLoader extends SchedulerBase {

    private static final Logger LOGGER = Logger.getLogger(FaplNewsLoader.class);

    @Autowired
    @Qualifier("resourceService")
    private ResourceService articleService;

    @Autowired
    private NewsFeedLoader feedLoader;

    Pattern pattern = Pattern.compile("http://fapl.ru/posts/(.*?)/");

    @Override
    public void execute() {
        List<Article> feedNews = feedLoader.loadNewsFeed(FAPL_FEED_URL);
        List<Article> postedArticles = new LinkedList<>();
        for (Article news : feedNews) {
            try {
                Matcher matcher = pattern.matcher(news.getLink());
                matcher.find();

                String newsId = matcher.group(1).trim();

                if (!articleService.exists("news:fapl:" + newsId)) {
                    LOGGER.info("New news: " + news.getTitle());
    
                    postedArticles.add(news);
                    LOGGER.info("Saving: " + news.getTitle());
                    Resource resource = new Resource();
                    resource.setId("news:fapl:" + newsId);
                    articleService.save(resource);
                } else {
                    LOGGER.info("Ignoring: " + news.getTitle());
                }
            } catch (Exception e) {
                throw new VkException("Exception loading news: " + news.getTitle(), e);
            }
        }
        if (postedArticles.size() > 0) {
            StringBuilder articlePart = new StringBuilder();
            for (Article postedArticle : postedArticles) {
                articlePart.append("• «").append(postedArticle.getTitle()).append("». Источник: ").append(postedArticle.getLink()).append(".\n");
            }
            vkGateway.sendChatMessage("Новые статьи c FAPL.ru: \n"
                    + articlePart, getChatId());
        }
    }

    public static void main(String[] args) {
        Matcher matcher = Pattern.compile("http://fapl.ru/posts/(.*?)/").matcher("http://fapl.ru/posts/54889/");
        matcher.find();

        String titlePhrase = matcher.group(1).trim();
        System.out.println(titlePhrase);
    }
}

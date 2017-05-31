package com.vk.tottenham.scheduler;

import static com.vk.tottenham.utils.NewsFeedLoader.OFFICIAL_FEED_URL;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.core.model.Resource;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.ContentBuilder;
import com.vk.tottenham.utils.JsoupWrapper;
import com.vk.tottenham.utils.NewsFeedLoader;
import com.vk.tottenham.utils.PhotoDownloader;

public class OfficialNewsLoader extends SchedulerBase {

    private static final Logger LOGGER = Logger.getLogger(OfficialNewsLoader.class);
    private static final String BASE_URL = "http://www.tottenhamhotspur.com";

    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "d MMMMM yyyy HH:mm");

    static {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Autowired
    private NewsFeedLoader feedLoader;
    @Autowired
    @Qualifier("resourceService")
    private ResourceService articleService;
    @Autowired
    private ContentBuilder contentBuilder;
    @Autowired
    private PhotoDownloader photoDownloader;
    @Autowired
    private JsoupWrapper jsoupWrapper;

    @Override
    public void execute() {
        StringBuilder articlePart = new StringBuilder();
        for (String postedArticle : getPostedArticles()) {
            articlePart.append("• «").append(postedArticle).append("»\n");
        }
        if (articlePart.length() > 0) {
            vkGateway.sendChatMessage("Новые статьи: \n" + articlePart, getChatId());
        }
    }

    private List<String> getPostedArticles() {
        List<Article> feedNews = feedLoader.loadNewsFeed(OFFICIAL_FEED_URL);
        List<String> postedArticles = new LinkedList<>();
        for (Article news : feedNews) {
            try {
                if (!articleService.exists("news:official:" + news.getId())) {
                    LOGGER.info("New news: " + news.getTitle());
    
                    String photoId = photoDownloader.downloadPhoto(news.getThumbnail(), isTestMode).getPhotoId();
    
                    List<String> galleryPhotoIds = loadGalleryPhotos(news.getLink());
                    if (galleryPhotoIds.isEmpty()) {
                        galleryPhotoIds.add(photoId);
                    }
    
                    String message = contentBuilder.buildPost(news.getTitle(), news.getDescription(), news.getContent(), news.getLink(), "tottenhamhotspur.com");
                    
                    LOGGER.info("Posting: " + news.getTitle());
                    vkGateway.postOnWall(getGroupId(), getMediaGroupId(), message.toString(), galleryPhotoIds, getClosestAvailableDate());
                    postedArticles.add(news.getTitle());

                    LOGGER.info("Saving: " + news.getTitle());
                    Resource resource = new Resource();
                    resource.setId("news:official:" + news.getId());
                    articleService.save(resource);
                } else {
                    LOGGER.info("Ignoring: " + news.getTitle());
                }
            } catch (Exception e) {
                //TODO: temporarily swallow this dude
            }
        }
        return postedArticles;
    }

    private List<String> loadGalleryPhotos(String link) {
        try {
            List<String> photoIds = new LinkedList<>();
            Document document = jsoupWrapper.getDocument((link));
            Elements imageContainers = document.getElementsByClass("galleryImages");
            for (Element imageContainer : imageContainers) {
                Elements images = imageContainer.getElementsByTag("img");
                for (Element image : images) {
                    String imageUrl = BASE_URL + image.attr("src").trim().replace("\n", "");
                    String photoId = photoDownloader.downloadPhoto(imageUrl, isTestMode).getPhotoId(); 
                    photoIds.add(photoId);
                }
            }
            return photoIds;
        } catch (Exception e) {
            throw new VkException("Error loading gallery.", e);
        }
    }
}

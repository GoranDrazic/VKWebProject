package com.vk.tottenham.scheduler;

import static com.vk.tottenham.utils.NewsFeedLoader.OFFICIAL_FEED_URL;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.core.model.NewsSource;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.utils.ContentBuilder;
import com.vk.tottenham.utils.JsoupWrapper;
import com.vk.tottenham.utils.PhotoDownloader;

public class OfficialNewsLoader extends NewsLoaderBase {

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
    private ContentBuilder contentBuilder;
    @Autowired
    private PhotoDownloader photoDownloader;
    @Autowired
    private JsoupWrapper jsoupWrapper;

    @Override
    protected NewsSource getNewsSource() {
        return NewsSource.OFFICIAL;
    }

    @Override
    protected String getArticleId(Article article) {
        return String.valueOf(article.getId());
    }

    @Override
    protected String getChatMessagePrefix() {
        return "Новые статьи c оффициального сайта";
    }

    @Override
    protected String getFeedURL() {
        return OFFICIAL_FEED_URL;
    }

    @Override
    protected String getArticle(Article news) {
        String photoId = photoDownloader.downloadPhoto(news.getThumbnail(), isTestMode).getPhotoId();

        List<String> galleryPhotoIds = loadGalleryPhotos(news.getLink());
        if (galleryPhotoIds.isEmpty()) {
            galleryPhotoIds.add(photoId);
        }

        String message = contentBuilder.buildPost(news.getTitle(), news.getDescription(), news.getContent(), news.getLink(), "tottenhamhotspur.com");
        
        vkGateway.postOnWall(getGroupId(), getMediaGroupId(), message.toString(), galleryPhotoIds, getClosestAvailableDate());

        return "«" + news.getTitle() + "»";
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

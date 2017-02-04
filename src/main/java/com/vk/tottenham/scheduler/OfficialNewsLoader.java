package com.vk.tottenham.scheduler;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.core.model.RSS;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ArticleService;
import com.vk.tottenham.utils.ContentBuilder;
import com.vk.tottenham.utils.Icon;
import com.vk.tottenham.utils.PhotoDownloader;

public class OfficialNewsLoader extends SchedulerBase {

    private static final Logger LOGGER = Logger.getLogger(OfficialNewsLoader.class);
    private static final String BASE_URL = "http://www.tottenhamhotspur.com";
    private static final String FEED_URL = "http://www.tottenhamhotspur.com/application/iphone/latestnews.xml";

    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "d MMMMM yyyy HH:mm");

    static {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private static HttpClient client = HttpClientBuilder.create().build();

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Autowired
    @Qualifier("articleService")
    private ArticleService articleService;
    @Autowired
    private ContentBuilder contentBuilder;
    @Autowired
    private PhotoDownloader photoDownloader;

    @Override
    public void execute() {
        List<Article> feedNews = loadNewsFeed();
        for (Article news : feedNews) {
            if (articleService.findById(news.getId()) == null) {
                LOGGER.info("New news: " + news.getTitle());

                String photoId = photoDownloader.downloadPhoto(news.getThumbnail(), isTestMode);

                List<String> galleryPhotoIds = loadGalleryPhotos(news.getLink());
                if (galleryPhotoIds.isEmpty()) {
                    galleryPhotoIds.add(photoId);
                }

                String message = contentBuilder.buildPost(news.getTitle(), news.getContent(), news.getLink(), "tottenhamhotspur.com", Icon.ARTICLE);
                
                LOGGER.info("Posting: " + news.getTitle());
                vkGateway.postOnWall(getGroupId(), message.toString(), galleryPhotoIds, getClosestAvailableDate());
                vkGateway.sendChatMessage("Новая статья: «"
                                + news.getTitle()
                                + "».", getChatId());
                LOGGER.info("Saving: " + news.getTitle());
                articleService.save(news);
            } else {
                LOGGER.info("Ignoring: " + news.getTitle());
            }
        }
    }

    private List<Article> loadNewsFeed() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RSS.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            HttpGet request = new HttpGet(FEED_URL);
            HttpResponse response = client.execute(request);

            RSS rss = (RSS) jaxbUnmarshaller
                    .unmarshal(response.getEntity().getContent());
            response.getEntity().getContent().close();
            return rss.getChannel().getItem();
        } catch (Exception e) {
            throw new VkException("Exception loading data from feed.", e);
        }
    }

    private List<String> loadGalleryPhotos(String link) {
        try {
            List<String> photoIds = new LinkedList<>();
            Document document = Jsoup.parse(new URL(link), 10000);
            Elements imageContainers = document.getElementsByClass("galleryImages");
            for (Element imageContainer : imageContainers) {
                Elements images = imageContainer.getElementsByTag("img");
                for (Element image : images) {
                    String imageUrl = BASE_URL + image.attr("src").trim().replace("/n", "");
                    String photoId = photoDownloader.downloadPhoto(imageUrl, isTestMode); 
                    photoIds.add(photoId);
                }
            }
            return photoIds;
        } catch (Exception e) {
            throw new VkException("Error loading gallery.", e);
        }
    }
}

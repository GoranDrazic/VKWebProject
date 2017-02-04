package com.vk.tottenham.scheduler;

import java.net.URL;

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
import com.vk.tottenham.core.model.Resource;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ResourceService;

public class YoutubeChecker extends SchedulerBase {
    private static final Logger LOGGER = Logger.getLogger(YoutubeChecker.class);

    @Autowired
    @Qualifier("resourceService")
    private ResourceService youtubeService;

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public void execute() {
        try {
            Document document = Jsoup.parse(
                    new URL("https://www.youtube.com/user/spursofficial/videos"), 10000);
            Element videoContainer = document.getElementsByAttributeValue("id", "channels-browse-content-grid").get(0);
            Elements videos = videoContainer.getElementsByClass("ux-thumb-wrap");
            for (Element video : videos) {
                Element link = video.getElementsByClass("yt-uix-sessionlink").last();
                String title = link.text();
                String href = link.attr("href");
                if (!youtubeService.exists("youtube:spurs_official:" + href)) {
                    Resource youtubeResource = new Resource();
                    youtubeResource.setId("youtube:spurs_official:" + href);
                    youtubeService.save(youtubeResource);
                    vkGateway.sendChatMessage(
                            "Официальный канал на ютюбе загрузил новое видео: " + title + "\nhttps://www.youtube.com" + href,
                            getChatId());
                }
            }
            Thread.currentThread().sleep(5*1000);
        } catch (Exception e) {
            throw new VkException("Error loading info about youtube video.", e);
        }
    }
}

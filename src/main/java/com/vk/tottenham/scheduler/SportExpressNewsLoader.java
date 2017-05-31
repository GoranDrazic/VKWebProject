package com.vk.tottenham.scheduler;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.core.model.NewsSource;
import com.vk.tottenham.core.model.ResourceType;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ResourceService;

public class SportExpressNewsLoader extends SchedulerBase {

    private static final String URL = "http://www.sport-express.ru/tags-tottenhem-512/";
    private static final String SHORT_URL = "http://www.sport-express.ru";

    @Autowired
    @Qualifier("resourceService")
    private ResourceService newsService;

    @Override
    public void execute() {
        try {
            Document content = Jsoup.parse(new URL(URL), 10000);
            Elements newsElements = content.getElementsByClass("materials_container")
                    .get(0).getElementsByClass("recent_item");
            for (Element newsElement : newsElements) {
                Element newsLink = newsElement.getElementsByClass("fs_20").get(0);
                
                String link = newsLink.attr("href");
                String title = newsLink.text();
                
                int lastSlash = link.replaceAll("/$", "").lastIndexOf("/");
                String id = link.substring(lastSlash + 1);
                
                if (!newsService.exists(ResourceType.NEWS.value(), NewsSource.SPORT_EXPRESS.value(), id)) {
                    vkGateway
                            .sendChatMessage("sport-express.ru опубликовали новую статью \""
                                    + title + "\"\n" + link, getChatId());
                    newsService.save(ResourceType.NEWS.value(), NewsSource.SPORT_EXPRESS.value(), id);
                    Thread.currentThread().sleep(5*1000);
                }
            }
        } catch (IOException e) {
            throw new VkException("Error loading spurs army news",e);
        } catch (InterruptedException e) {
            throw new VkException("Error loading spurs army news",e);
        }
    }
}

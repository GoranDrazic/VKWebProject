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

public class SportsRuNewsLoader extends SchedulerBase {

    private static final String URL = "http://www.sport.ru/football/tottenhem_angliya";
    private static final String SHORT_URL = "http://www.sport.ru";

    @Autowired
    @Qualifier("resourceService")
    private ResourceService newsService;

    @Override
    public void execute() {
        try {
            Document content = Jsoup.parse(new URL(URL), 10000);
            Elements newsElements = content.getElementsByClass("articles-item-large");
            for (Element newsElement : newsElements) {
                Element newsLink = newsElement.getElementsByTag("h3").get(0)
                        .getElementsByTag("a").get(0);
                
                String link = SHORT_URL + newsLink.attr("href").replaceAll("/$", "");
                String title = newsLink.text();
                
                int lastSlash = link.lastIndexOf("/");
                String id = link.substring(lastSlash + 1);
                
                if (!newsService.exists(ResourceType.NEWS.value(), NewsSource.SPURS_RU.value(), id)) {
                    vkGateway
                            .sendChatMessage("Sport.ru опубликовали новую статью \""
                                    + title + "\"\n" + link, getChatId());
                    newsService.save(ResourceType.NEWS.value(), NewsSource.SPURS_RU.value(), id);
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

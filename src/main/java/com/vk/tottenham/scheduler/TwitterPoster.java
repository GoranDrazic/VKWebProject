package com.vk.tottenham.scheduler;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.core.model.Resource;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.PhotoDownloader;

public class TwitterPoster extends SchedulerBase {

    @Autowired
    @Qualifier("resourceService")
    private ResourceService twitterService;

    @Autowired
    private PhotoDownloader photoDownloader;

    private List<String> accounts;

    @Override
    public void execute() {
        for (String account: accounts) {
            try {
                Document content = Jsoup
                        .parse(new URL("https://twitter.com/" + account), 10000);
                Elements twitElements = content.getElementsByClass("js-stream-item");
                LinkedList<Element> twitList = new LinkedList<>(twitElements);
                Iterator<Element> it = twitList.descendingIterator();
                while (it.hasNext()) {
                    Element twitElement = it.next();
                    String href = twitElement
                            .getElementsByClass("tweet-timestamp").attr("href");
                    System.out.println("href: " + href);
                    String twitText = twitElement
                            .getElementsByClass("js-tweet-text-container").text();
                    System.out.println("text: " + twitText);
                    Elements photoElements = twitElement
                            .getElementsByClass("AdaptiveMedia-photoContainer");
                    String imgSrc = null;
                    for (Element photoElement : photoElements) {
                        Elements imgElements = photoElement.getElementsByTag("img");
                        for (Element imgElement : imgElements) {
                            imgSrc = imgElement.attr("src");
                            System.out.println("img: " + imgElement.attr("src"));
                        }
                    }
                    if(!twitterService.exists("twitter:" + account + ":" + href)) {
                        Resource resource = new Resource();
                        resource.setId("twitter:" + account + ":" + href);
                        twitterService.save(resource);
                        
                        String photoId = null;
                        if (imgSrc != null) {
                            photoId = photoDownloader.downloadPhoto(imgSrc, isTestMode);
                        }
                        vkGateway.sendChatMessage(account + " твитнул: «"
                                + twitText
                                + "».", getChatId(), photoId, getGroupId());
                    }
                    Thread.currentThread().sleep(5*1000);
                }
            } catch (Exception e) {
                throw new VkException("Error loading page.", e);
            }
        }
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }
}

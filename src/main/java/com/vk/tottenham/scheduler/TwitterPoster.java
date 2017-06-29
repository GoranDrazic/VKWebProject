package com.vk.tottenham.scheduler;

import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.collect.Lists;
import com.vk.tottenham.core.model.ResourceType;
import com.vk.tottenham.core.model.Twit;
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
                //List<Twit> twits = extractTwits(it);
                //twits = mergeByName(twits);
                while (it.hasNext()) {
                    Element twitElement = it.next();
                    String href = twitElement
                            .getElementsByClass("tweet-timestamp").attr("href");
                    String twitText = twitElement
                            .getElementsByClass("js-tweet-text-container").text();
                    Elements photoElements = twitElement
                            .getElementsByClass("AdaptiveMedia-photoContainer");
                    //String duration = twitElement.getElementsByClass("js-relative-timestamp").get(0).text();
                    String imgSrc = null;
                    for (Element photoElement : photoElements) {
                        Elements imgElements = photoElement.getElementsByTag("img");
                        for (Element imgElement : imgElements) {
                            imgSrc = imgElement.attr("src");
                        }
                    }
                    if(!twitterService.exists(ResourceType.TWITTER.value(), account, href)) {
                        String photoId = null;
                        if (imgSrc != null) {
                            photoId = photoDownloader.downloadPhoto(imgSrc, isTestMode).getPhotoId();
                        }
                        vkGateway.sendChatMessage(account + " твитнул: «"
                                + twitText
                                + "».", getChatId(), photoId, getMediaGroupId());

                        twitterService.save(ResourceType.TWITTER.value(), account, href);
                    }
                    Thread.currentThread().sleep(5*1000);
                }
            } catch (Exception e) {
                throw new VkException("Error loading page.", e);
            }
        }
    }

    private List<Twit> mergeByName(List<Twit> twits) {
        List<Twit> result = Lists.newArrayList();
        Twit lastTwit = null;
        for (Twit twit : twits) {
            if (twitsShouldBeMerged(lastTwit, twit)) {
                mergeTwits(lastTwit, twit);
            } else {
                result.add(twit);
                lastTwit = twit;
            }
        }
        return result;
    }

    private void mergeTwits(Twit lastTwit, Twit twit) {
        lastTwit.appendTwitText(twit.getTwitText());
    }

    private boolean twitsShouldBeMerged(Twit lastTwit, Twit twit) {
        return lastTwit != null && lastTwit.getPerson() != null
                && twit.getPerson() != null
                && lastTwit.getPerson().equals(twit.getPerson());
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    private List<Twit> extractTwits(Iterator<Element> it) {
        List<Twit> result = Lists.newArrayList();
        while (it.hasNext()) {
            Element twitElement = it.next();

            Twit twit = new Twit();
            twit.setHref(twitElement.getElementsByClass("tweet-timestamp").attr("href"));
            String rawTwitText = twitElement.getElementsByClass("js-tweet-text-container").text();
            twit.setPerson(extractPerson(rawTwitText));
            twit.setTwitText(extractTwitText(rawTwitText));
            Elements photoElements = twitElement.getElementsByClass("AdaptiveMedia-photoContainer");
            //twit.setElapsedTime(extractElapsedTime(twitElement.getElementsByClass("js-relative-timestamp").get(0).text()));
            for (Element photoElement : photoElements) {
                Elements imgElements = photoElement.getElementsByTag("img");
                for (Element imgElement : imgElements) {
                    twit.addPhotoElement(imgElement.attr("src"));
                }
            }
            result.add(twit);
        }
        return result;
    }

    private int extractElapsedTime(String timeString) {
        int result;

        int length = timeString.length();

        char timeUnit = timeString.charAt(length - 1);
        int elapsedTime = Integer.valueOf(timeString.substring(0, length - 1));
        
        switch (timeUnit) {
        case 's':
            result = elapsedTime;
            break;
        case 'm':
            result = elapsedTime*60;
            break;
        case 'h':
            result = elapsedTime*60*60;
            break;
        case 'd':
            result = elapsedTime*60*60*24;
            break;
        default:
            result = Integer.MAX_VALUE;
            break;
        }
        return result;
    }

    private String extractTwitText(String rawTwitText) {
        String trimmedString = rawTwitText.replace(" #THFC", "");
        if (rawTwitText.indexOf(':') > 0 && rawTwitText.indexOf(':') < rawTwitText.indexOf('"')) {
            trimmedString = trimmedString.substring(rawTwitText.indexOf(':') + 1);
            trimmedString = trimmedString.replace("\"", "").trim();
        }
        return trimmedString;
    }

    private String extractPerson(String rawTwitText) {
        String trimmedString = rawTwitText.replace(" #THFC", "");
        if (rawTwitText.indexOf(':') > 0 && rawTwitText.indexOf(':') < rawTwitText.indexOf('"')) {
            trimmedString = trimmedString.substring(0, rawTwitText.indexOf(':'));
        }
        return null;
    }
    
    public static void main(String[] args) {
        System.out.println(new Date(1497044401000l));
    }
}

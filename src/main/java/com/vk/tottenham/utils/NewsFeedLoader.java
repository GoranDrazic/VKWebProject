package com.vk.tottenham.utils;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.vk.tottenham.core.model.Article;
import com.vk.tottenham.core.model.RSS;
import com.vk.tottenham.exception.VkException;

public class NewsFeedLoader {
    public static final String OFFICIAL_FEED_URL = "http://www.tottenhamhotspur.com/application/iphone/latestnews.xml";
    public static final String FAPL_FEED_URL = "http://fapl.ru/tags/%D2%EE%F2%F2%E5%ED%F5%FD%EC/rss/";

    private static HttpClient client = HttpClientBuilder.create().build();

    public List<Article> loadNewsFeed(String url) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RSS.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            RSS rss = (RSS) jaxbUnmarshaller
                    .unmarshal(response.getEntity().getContent());
            response.getEntity().getContent().close();
            return rss.getChannel().getItem();
        } catch (Exception e) {
            throw new VkException("Exception loading data from feed.", e);
        }
    }
}

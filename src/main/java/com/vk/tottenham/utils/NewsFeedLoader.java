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
    private static final String FEED_URL = "http://www.tottenhamhotspur.com/application/iphone/latestnews.xml";

    private static HttpClient client = HttpClientBuilder.create().build();

    public List<Article> loadNewsFeed() {
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
}

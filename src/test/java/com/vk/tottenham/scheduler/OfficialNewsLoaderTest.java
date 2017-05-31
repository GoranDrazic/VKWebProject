package com.vk.tottenham.scheduler;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.vk.tottenham.core.model.Resource;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.ContentBuilder;
import com.vk.tottenham.utils.JsoupWrapper;
import com.vk.tottenham.utils.NewsFeedLoader;
import com.vk.tottenham.utils.PhotoDownloader;
import com.vk.tottenham.vk.model.PhotoDescription;

public class OfficialNewsLoaderTest extends SchedulerBaseTest {
    private final static String LANE_MESSAGE = "Award for The Lane book\n\n\nThe Lane, the club’s critically-acclaimed book on the history of White Hart Lane, was last night named Best Illustrated Book at the prestigious Cross Sports Book Awards 2017, which were presented at a glittering ceremony at Lord’s Cricket Ground.\n\n\nAlready a huge bestseller in Spurs shops.\n\n\n\nOriginal article: \nhttp://www.tottenhamhotspur.com/news/shop-spurs/the-lane-book-wins-award-250517/\n\n\n(Источник: tottenhamhotspur.com)";
    private final static String LEDLEY_MESSAGE = "Meet Ledley at half-term Soccer Schools\n\n\nYoungsters on our half-term Soccer Schools are in for a real treat with the news that our legendary former captain Ledley King will be making a guest appearance at each course during the week.\n\n\nLedley remains a real fans' favourite.\n\nOriginal article: \nhttp://www.tottenhamhotspur.com/news/meet-ledley-at-half-term-soccer-schools-250517/\n\n\n(Источник: tottenhamhotspur.com)";
    private final static String CHAT_MESSAGE = "Новые статьи: \n• «Award for The Lane book»\n• «Meet Ledley at half-term Soccer Schools»\n";
    private final static String CHAT_MESSAGE_AFTER_EXCEPTION = "Новые статьи: \n• «Meet Ledley at half-term Soccer Schools»\n";

    private OfficialNewsLoader newsLoader;
    private NewsFeedLoader feedLoader;

    @Mock
    private ResourceService articleService;

    private ContentBuilder contentBuilder;
    @Mock
    private PhotoDownloader photoDownloader;
    @Mock
    private HttpClient httpClient;
    @Mock
    private JsoupWrapper jsoupWrapper;

    @Before
    public void init() {
        try {
            MockitoAnnotations.initMocks(this);
            super.init();
            newsLoader = new OfficialNewsLoader();
            feedLoader = new NewsFeedLoader();
            contentBuilder = new ContentBuilder();

            ReflectionTestUtils.setField(newsLoader, "feedLoader", feedLoader);
            ReflectionTestUtils.setField(newsLoader, "articleService", articleService);
            ReflectionTestUtils.setField(newsLoader, "contentBuilder", contentBuilder);
            ReflectionTestUtils.setField(newsLoader, "photoDownloader", photoDownloader);
            ReflectionTestUtils.setField(newsLoader, "jsoupWrapper", jsoupWrapper);
            ReflectionTestUtils.setField(feedLoader, "client", httpClient);

            ReflectionTestUtils.setField(newsLoader, "vkContext", vkContext);
            ReflectionTestUtils.setField(newsLoader, "vkGateway", vkGateway);

            InputStream stream = getClass().getResourceAsStream("officialNewsFeed.xml");
            HttpResponse httpResponse = mock(HttpResponse.class);
            when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

            HttpEntity httpEntity = mock(HttpEntity.class);
            when(httpResponse.getEntity()).thenReturn(httpEntity);
            when(httpEntity.getContent()).thenReturn(stream);
        } catch (Exception e) {
            // swallow
        }
    }

    @Test
    public void testPostTwoArticles() {
        when(articleService.exists("news:official:6442530672")).thenReturn(true);
        when(articleService.exists("news:official:6442530649")).thenReturn(true);

        when(photoDownloader.downloadPhoto(contains("lane_book_award_730"),
                eq(false))).thenReturn(new PhotoDescription("lane_book_award_730"));
        when(photoDownloader.downloadPhoto(contains("ledley_new730"), eq(false)))
                .thenReturn(new PhotoDescription("ledley_new730"));

        Document document = mock(Document.class);
        when(jsoupWrapper.getDocument(anyString())).thenReturn(document);
        when(document.getElementsByClass(eq("galleryImages"))).thenReturn(new Elements());

        newsLoader.execute();

        verify(articleService, times(4)).exists(anyString());
        verify(articleService, times(2)).save(any(Resource.class));

        verify(vkGateway, times(2)).getScheduledPosts(eq(GROUP_ID));
        verify(vkGateway).postOnWall(eq(GROUP_ID), eq(MEDIA_GROUP_ID),
                eq(LANE_MESSAGE), eq(Lists.newArrayList("lane_book_award_730")), anyLong());
        verify(vkGateway).postOnWall(eq(GROUP_ID), eq(MEDIA_GROUP_ID),
                eq(LEDLEY_MESSAGE), eq(Lists.newArrayList("ledley_new730")), anyLong());
        verify(vkGateway).sendChatMessage(eq(CHAT_MESSAGE), eq(CHAT_ID));

        verifyNoMoreInteractions(articleService, vkGateway);
    }

    @Test
    public void testGalleryIsPosted() {
        when(articleService.exists(not(eq("news:official:6442530656")))).thenReturn(true);

        for (String src: Lists.newArrayList("lane_book_award_730", "ledley_new730", "/tr_may12_730a.jpg", "/tr_may12_730d.jpg")) {
            when(photoDownloader.downloadPhoto(contains(src),
                    eq(false))).thenReturn(new PhotoDescription(src));
        }

        Document document = mock(Document.class);
        when(jsoupWrapper.getDocument(anyString())).thenReturn(document);
        Elements elements = new Elements();

        for (String src: Lists.newArrayList("/tr_may12_730a.jpg", "/tr_may12_730d.jpg")) {
            Attributes attributes = new Attributes();
            attributes.put("src", src);
            Element element = new Element(Tag.valueOf("img"), "baseUri", attributes );
            elements.add(element );
        }
        
        when(document.getElementsByClass(eq("galleryImages"))).thenReturn(elements);

        newsLoader.execute();

        verify(articleService, times(4)).exists(anyString());
        verify(articleService, times(1)).save(any(Resource.class));

        verify(vkGateway).getScheduledPosts(eq(GROUP_ID));
        verify(vkGateway).postOnWall(eq(GROUP_ID), eq(MEDIA_GROUP_ID),
                eq(LEDLEY_MESSAGE), eq(Lists.newArrayList("/tr_may12_730a.jpg", "/tr_may12_730d.jpg")), anyLong());
        verify(vkGateway).sendChatMessage(eq(CHAT_MESSAGE_AFTER_EXCEPTION), eq(CHAT_ID));

        verifyNoMoreInteractions(articleService, vkGateway);
    }

    @Test
    public void testNoNewArticles() {
        when(articleService.exists(anyString())).thenReturn(true);

        newsLoader.execute();

        verify(articleService, times(4)).exists(anyString());

        verifyNoMoreInteractions(articleService, vkGateway);
    }

    @Test
    public void testSingleLoadFailure() {
        when(articleService.exists("news:official:6442530672")).thenReturn(true);
        when(articleService.exists("news:official:6442530649")).thenReturn(true);

        when(photoDownloader.downloadPhoto(contains("lane_book_award_730"),
                eq(false))).thenReturn(new PhotoDescription("lane_book_award_730"));
        when(photoDownloader.downloadPhoto(contains("ledley_new730"), eq(false)))
                .thenReturn(new PhotoDescription("ledley_new730"));

        Document document = mock(Document.class);
        when(jsoupWrapper.getDocument(anyString())).thenReturn(document);
        when(document.getElementsByClass(eq("galleryImages"))).thenReturn(new Elements());

        doThrow(new VkException("Some crap happened.")).when(vkGateway).postOnWall(anyString(), anyString(), anyString(), eq(Lists.newArrayList("lane_book_award_730")), anyLong());

        newsLoader.execute();

        verify(articleService, times(4)).exists(anyString());
        verify(articleService, times(1)).save(any(Resource.class));

        verify(vkGateway, times(2)).getScheduledPosts(eq(GROUP_ID));
        verify(vkGateway).postOnWall(eq(GROUP_ID), eq(MEDIA_GROUP_ID),
                eq(LANE_MESSAGE), eq(Lists.newArrayList("lane_book_award_730")), anyLong());
        verify(vkGateway).postOnWall(eq(GROUP_ID), eq(MEDIA_GROUP_ID),
                eq(LEDLEY_MESSAGE), eq(Lists.newArrayList("ledley_new730")), anyLong());
        verify(vkGateway).sendChatMessage(eq(CHAT_MESSAGE_AFTER_EXCEPTION), eq(CHAT_ID));

        verifyNoMoreInteractions(articleService, vkGateway);
    }
}

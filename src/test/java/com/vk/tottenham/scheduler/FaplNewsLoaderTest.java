package com.vk.tottenham.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.vk.tottenham.core.model.NewsSource;
import com.vk.tottenham.core.model.ResourceType;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.NewsFeedLoader;

public class FaplNewsLoaderTest extends SchedulerBaseTest {
    private static final String CHAT_MESSAGE = "Новые статьи c FAPL.ru: \n• «Янссен поговорит с \"Тоттенхэмом\" о своем будущем». Источник: http://fapl.ru/posts/55239/.\n• «\"Юнайтед\" нацелился на Данни Роуза». Источник: http://fapl.ru/posts/55217/.\n";
    private final static String CHAT_MESSAGE_AFTER_EXCEPTION = "Новые статьи c FAPL.ru: \n• «Янссен поговорит с \"Тоттенхэмом\" о своем будущем». Источник: http://fapl.ru/posts/55239/.\n";
    
    private FaplNewsLoader newsLoader;
    private NewsFeedLoader feedLoader;

    @Mock
    private HttpClient httpClient;
    @Mock
    private ResourceService articleService;

    @Before
    public void init() {
        try {
            MockitoAnnotations.initMocks(this);
            super.init();
            
            newsLoader = new FaplNewsLoader();
            feedLoader = new NewsFeedLoader();
            ReflectionTestUtils.setField(newsLoader, "feedLoader", feedLoader);
            ReflectionTestUtils.setField(newsLoader, "articleService", articleService);
            ReflectionTestUtils.setField(feedLoader, "client", httpClient);

            ReflectionTestUtils.setField(newsLoader, "vkContext", vkContext);
            ReflectionTestUtils.setField(newsLoader, "vkGateway", vkGateway);

            InputStream stream = getClass()
                    .getResourceAsStream("faplNewsFeed.xml");
            HttpResponse httpResponse = mock(HttpResponse.class);
            when(httpClient.execute(any(HttpUriRequest.class)))
                    .thenReturn(httpResponse);

            HttpEntity httpEntity = mock(HttpEntity.class);
            when(httpResponse.getEntity()).thenReturn(httpEntity);
            when(httpEntity.getContent()).thenReturn(stream);
        } catch (Exception e) {
            // swallow
        }
    }
    
    @Test
    public void testPostTwoArticles() {
        when(articleService.exists(ResourceType.NEWS.value(), NewsSource.FAPL.value(), "55229")).thenReturn(true);
        when(articleService.exists(ResourceType.NEWS.value(), NewsSource.FAPL.value(), "55218")).thenReturn(true);

        newsLoader.execute();

        verify(articleService, times(4)).exists(eq(ResourceType.NEWS.value()), eq(NewsSource.FAPL.value()), anyString());
        verify(articleService, times(2)).save(eq(ResourceType.NEWS.value()), eq(NewsSource.FAPL.value()), anyString());
        verify(vkGateway).sendChatMessage(eq(CHAT_MESSAGE), eq(CHAT_ID));

        verifyNoMoreInteractions(articleService, vkGateway);
    }

    @Test
    public void testNoNewArticles() {
        when(articleService.exists(eq(ResourceType.NEWS.value()), eq(NewsSource.FAPL.value()), anyString())).thenReturn(true);

        newsLoader.execute();

        verify(articleService, times(4)).exists(eq(ResourceType.NEWS.value()), eq(NewsSource.FAPL.value()), anyString());

        verifyNoMoreInteractions(articleService, vkGateway);
    }

    @Test
    public void testSingleLoadFailure() {
        when(articleService.exists(ResourceType.NEWS.value(), NewsSource.FAPL.value(), "55229")).thenReturn(true);
        when(articleService.exists(ResourceType.NEWS.value(), NewsSource.FAPL.value(), "55218")).thenReturn(true);
        when(articleService.exists(ResourceType.NEWS.value(), NewsSource.FAPL.value(), "55239")).thenReturn(false);
        doThrow(new VkException("Some crap happened.")).when(articleService).exists(ResourceType.NEWS.value(), NewsSource.FAPL.value(), "55217");

        newsLoader.execute();

        verify(articleService, times(4)).exists(eq(ResourceType.NEWS.value()), eq(NewsSource.FAPL.value()), anyString());
        verify(articleService).save(eq(ResourceType.NEWS.value()), eq(NewsSource.FAPL.value()), eq("55239"));
        verify(vkGateway).sendChatMessage(eq(CHAT_MESSAGE_AFTER_EXCEPTION), eq(CHAT_ID));

        verifyNoMoreInteractions(articleService, vkGateway);
    }
}

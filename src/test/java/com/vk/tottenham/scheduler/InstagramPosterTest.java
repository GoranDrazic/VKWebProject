package com.vk.tottenham.scheduler;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.core.model.ResourceType;
import com.vk.tottenham.instagram.InstagramObjectMapper;
import com.vk.tottenham.mybatis.service.PlayerService;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.JsoupWrapper;
import com.vk.tottenham.utils.PhotoDownloader;
import com.vk.tottenham.vk.model.PhotoDescription;

public class InstagramPosterTest extends SchedulerBaseTest {

    private InstagramPoster poster;

    @Mock
    private PlayerService playerService;
    @Mock
    private HttpClient httpClient;
    @Mock
    private JsoupWrapper jsoupWrapper;
    @Mock
    private ResourceService instagramService;
    @Mock
    private PhotoDownloader photoDownloader;

    private InstagramObjectMapper mapper;

    @Before
    public void init() {
        try {
            MockitoAnnotations.initMocks(this);
            super.init();

            poster = new InstagramPoster();
            mapper = new InstagramObjectMapper();
            ReflectionTestUtils.setField(poster, "playerService", playerService);
            ReflectionTestUtils.setField(poster, "jsoupWrapper", jsoupWrapper);
            ReflectionTestUtils.setField(poster, "mapper", mapper);
            ReflectionTestUtils.setField(poster, "instagramService", instagramService);
            ReflectionTestUtils.setField(poster, "photoDownloader", photoDownloader);

            ReflectionTestUtils.setField(poster, "vkContext", vkContext);
            ReflectionTestUtils.setField(poster, "vkGateway", vkGateway);
        } catch (Exception e) {
            // swallow
        }
    }

    @Test
    public void testTwoAccounts() throws IOException {
        when(playerService.findAll()).thenReturn(createPlayerList());
        InputStream stream = getClass().getResourceAsStream("toby.html");
        when(jsoupWrapper.getDocument("https://www.instagram.com/toby/")).thenReturn(Jsoup.parse(stream, "UTF-8", "https://www.instagram.com/toby/"));
        stream = getClass().getResourceAsStream("chris.html");
        when(jsoupWrapper.getDocument("https://www.instagram.com/chris/")).thenReturn(Jsoup.parse(stream, "UTF-8", "https://www.instagram.com/chris/"));

        for (String name : Lists.newArrayList("toby", "chris")) {
            for (int i=0; i<12; i++) {
                PhotoDescription pd = new PhotoDescription();
                pd.setPhotoId(name.charAt(0) + "d" + i + ".jpg");
                when(photoDownloader.downloadPhoto(eq("https://instagram/" + name.charAt(0) + "d" + i + ".jpg"), eq(false))).thenReturn(pd);
                if (i % 4 == 3) {
                    when(instagramService.exists(eq(ResourceType.INSTAGRAM.value()), anyString(), eq(name.charAt(0) + "d" + i + ".jpg"))).thenReturn(true);
                }
            }
        }

        poster.execute();

        for (String name : Lists.newArrayList("toby", "chris")) {
            for (int i=0; i<12; i++) {
                if(i % 6 != 5 || !"toby".equals(name)) {
                    verify(instagramService).exists(eq(ResourceType.INSTAGRAM.value()), anyString(), eq(name.charAt(0) + "d" + i + ".jpg"));
                    if (i % 4 != 3) {
                        verify(instagramService).save(eq(ResourceType.INSTAGRAM.value()), anyString(), eq(name.charAt(0) + "d" + i + ".jpg"));
                    }
                }
            }
        }

        verify(vkGateway, times(2)).getScheduledPosts(eq("45645234625"));
        verify(vkGateway, times(2)).postOnWall(eq("45645234625"), eq("72345234542"), anyString(), anyList(), anyLong());
        verify(vkGateway, times(2)).sendChatMessage(eq("В отложку добавлены фото из соц сетей. Пожалуйста, проверьте и запостите их."), eq("823452345234"));

        verifyNoMoreInteractions(instagramService, vkGateway);
    }

    private List<Player> createPlayerList() {
        List<Player> list = Lists.newArrayListWithExpectedSize(3);
        list.add(createPlayer("toby"));
        list.add(createPlayer("chris"));
        list.add(createPlayer(null));
        return list;
    }

    private Player createPlayer(String instagram) {
        Player player = new Player();
        player.setInstagram(instagram);
        return player ;
    }
}

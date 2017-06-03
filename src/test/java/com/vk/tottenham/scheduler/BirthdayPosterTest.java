package com.vk.tottenham.scheduler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.mybatis.service.PlayerService;

public class BirthdayPosterTest extends SchedulerBaseTest {

    private BirthdayPoster birthdayPoster;

    @Mock
    private PlayerService playerService;

    @Before
    public void init() {
        try {
            MockitoAnnotations.initMocks(this);
            super.init();
            
            birthdayPoster = new BirthdayPoster();
            ReflectionTestUtils.setField(birthdayPoster, "playerService", playerService);

            ReflectionTestUtils.setField(birthdayPoster, "vkContext", vkContext);
            ReflectionTestUtils.setField(birthdayPoster, "vkGateway", vkGateway);

        } catch (Exception e) {
            // swallow
        }
    }
    
    @Test
    public void testPostTwoArticles() {
        when(playerService.findBirthdayBoys()).thenReturn(getBirthdayBoys());

        birthdayPoster.execute();

        verify(vkGateway).sendChatMessage(eq("Завтра день рождения у Toby Alderweireld, Mauricio Pochettino. Пожалуйста сделайте пост."), 
                eq(CHAT_ID), anyString(), eq("125436039"));
    }

    private List<Player> getBirthdayBoys() {
        List<Player> resultList = Lists.newArrayListWithExpectedSize(2);
        resultList.add(createPlayer("Toby", "Alderweireld"));
        resultList.add(createPlayer("Mauricio", "Pochettino"));
        return resultList;
    }

    private Player createPlayer(String name, String surname) {
        Player player = new Player();
        player.setName(name);
        player.setSurname(surname);
        return player ;
    }
}

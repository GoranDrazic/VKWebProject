package com.vk.tottenham.scheduler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.mybatis.service.PlayerService;

public class BirthdayPoster extends SchedulerBase {
    private static final String[] photos = new String[] { "456244168", "456244169",
            "456244170", "456244171", "456244172" };

    @Autowired
    private PlayerService playerService;

    public void execute() {
        List<Player> birthdayBoys = playerService.findBirthdayBoys();
        if (birthdayBoys.size() > 0) {
            String photo = photos[(int)(Math.random() * 1000) % photos.length];
            List<String> names = (List<String>) birthdayBoys.stream().map(player -> player.getName() + " " + player.getSurname()).collect(Collectors.toList());
            vkGateway.sendChatMessage("Завтра день рождения у "
                    + String.join(", ", names) + 
                    ". Пожалуйста сделайте пост.", getChatId(), photo, "125436039");
        }
    }
}

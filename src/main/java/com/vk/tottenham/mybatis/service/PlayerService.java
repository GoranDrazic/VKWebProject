package com.vk.tottenham.mybatis.service;

import java.util.List;

import com.vk.tottenham.core.model.Player;

public interface PlayerService {
    List<Player> findAll();
    void save(Player player);
    void update(Player player);
    List<Player> findBirthdayBoys();
    Player findNameAndSurname(Player player);
}

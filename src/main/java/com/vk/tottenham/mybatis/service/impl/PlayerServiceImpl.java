package com.vk.tottenham.mybatis.service.impl;

import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.mybatis.mappers.PlayerMapper;
import com.vk.tottenham.mybatis.service.PlayerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("playerService")
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public List<Player> findAll() {
        return playerMapper.findAll();
    }

    @Override
    public void save(Player player) {
        playerMapper.save(player);
    }

    @Override
    public void update(Player player) {
        playerMapper.update(player);
    }

    @Override
    public List<Player> findBirthdayBoys() {
        return playerMapper.findBirthdayBoys();
    }

    @Override
    public Player findNameAndSurname(Player player) {
        return playerMapper.findNameAndSurname(player);
    }
}

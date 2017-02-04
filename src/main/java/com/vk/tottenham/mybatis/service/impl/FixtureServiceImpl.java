package com.vk.tottenham.mybatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vk.tottenham.core.model.Fixture;
import com.vk.tottenham.mybatis.mappers.FixtureMapper;
import com.vk.tottenham.mybatis.service.FixtureService;

@Service("fixtureService")
public class FixtureServiceImpl implements FixtureService {
    @Autowired
    private FixtureMapper fixtureMapper;

    @Override
    public Fixture find(int id) {
        return fixtureMapper.find(id);
    }

    @Override
    public void save(Fixture fixture) {
        fixtureMapper.save(fixture);
    }

    @Override
    public void update(Fixture fixture) {
        fixtureMapper.update(fixture);
    }

    @Override
    public Fixture getNearestFixture() {
        return fixtureMapper.getNearestFixture();
    }

    @Override
    public String getAlbumId(Fixture nearestFixture) {
        return fixtureMapper.getAlbumId(nearestFixture);
    }

    @Override
    public void setZimbioAlbumId(Fixture fixture) {
        fixtureMapper.setZimbioAlbumId(fixture);
    }

    @Override
    public void setAlbumId(Fixture fixture) {
        fixtureMapper.setAlbumId(fixture);
    }

    @Override
    public Fixture getPreviousFixture(String competition) {
        return fixtureMapper.getPreviousFixture(competition);
    }

    @Override
    public Fixture getNextFixture(String competition) {
        return fixtureMapper.getNextFixture(competition);
    }
}

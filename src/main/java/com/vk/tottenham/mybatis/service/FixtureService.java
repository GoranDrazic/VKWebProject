package com.vk.tottenham.mybatis.service;

import com.vk.tottenham.core.model.Fixture;

public interface FixtureService {

    Fixture find(int id);

    void save(Fixture fixture);

    void update(Fixture fixture);

    Fixture getNearestFixture();

    String getAlbumId(Fixture nearestFixture);

    void setZimbioAlbumId(Fixture fixture);

    void setAlbumId(Fixture fixture);

    Fixture getPreviousFixture(String competition);

    Fixture getNextFixture(String competition);
    
    Fixture getCurrentFixture();
}

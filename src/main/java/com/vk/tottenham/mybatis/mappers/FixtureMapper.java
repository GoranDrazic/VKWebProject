package com.vk.tottenham.mybatis.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.vk.tottenham.core.model.Fixture;

public interface FixtureMapper {

    @Select("SELECT id as id, date as date, "
            + "homeTeam as homeTeam, awayTeam as awayTeam, "
            + "competition as competition, season as season, "
            + "result as result, stadium as stadium, "
            + "gameweek as gameweek, city as city "
            + "FROM Fixtures "
            + "WHERE id=#{id}")
    Fixture find(int id);

    @Insert("INSERT INTO Fixtures(id, date, homeTeam, awayTeam,"
            + "competition, season, result, stadium, gameweek, city) VALUES"
            + "(#{id}, #{date},#{homeTeam}, #{awayTeam}, #{competition},"
            + "#{season}, #{result}, #{stadium}, #{gameweek}, #{city})")
    //@Options(useGeneratedKeys = true, keyProperty = "id", flushCache = true, keyColumn = "id")
    public void save(Fixture fixture);

    @Insert("UPDATE Fixtures set date=#{date},homeTeam=#{homeTeam},"
            + "awayTeam=#{awayTeam},competition=#{competition},"
            + "season=#{season}, result=#{result},"
            + "stadium=#{stadium}, gameweek=#{gameweek}, city=#{city} "
            + "WHERE id=#{id}")
    void update(Fixture fixture);

    @Select("SELECT id as id, `date` as date, "
            + "homeTeam as homeTeam, awayTeam as awayTeam, "
            + "competition as competition, season as season, "
            + "result as result, stadium as stadium, "
            + "albumId as albumId, zimbioAlbumId as zimbioAlbumId, "
            + "gameweek as gameweek, city as city "
            + "FROM Fixtures "
            + "WHERE date < DATE_ADD(now(), INTERVAL 1 DAY) "
            + "AND date > DATE_SUB(now(), INTERVAL 1 DAY) "
            + "LIMIT 1")
    Fixture getNearestFixture();

    @Select("SELECT albumId as albumId "
            + "FROM Fixtures "
            + "WHERE id=#{id}")
    String getAlbumId(Fixture nearestFixture);

    @Select("UPDATE Fixtures "
            + "SET zimbioAlbumId=#{zimbioAlbumId} "
            + "WHERE id=#{id}")
    void setZimbioAlbumId(Fixture fixture);

    @Select("UPDATE Fixtures "
            + "SET albumId=#{albumId} "
            + "WHERE id=#{id}")
    void setAlbumId(Fixture fixture);

    @Select("SELECT id as id, `date` as date, "
            + "homeTeam as homeTeam, awayTeam as awayTeam, "
            + "competition as competition, season as season, "
            + "result as result, stadium as stadium, "
            + "albumId as albumId, zimbioAlbumId as zimbioAlbumId, "
            + "gameweek as gameweek, city as city "
            + "FROM Fixtures WHERE date < now() AND competition=#{competition} "
            + "ORDER BY date DESC LIMIT 1")
    Fixture getPreviousFixture(String competition);

    @Select("SELECT id as id, `date` as date, "
            + "homeTeam as homeTeam, awayTeam as awayTeam, "
            + "competition as competition, season as season, "
            + "result as result, stadium as stadium, "
            + "albumId as albumId, zimbioAlbumId as zimbioAlbumId, "
            + "gameweek as gameweek, city as city "
            + "FROM Fixtures "
            + "WHERE date > now() AND competition=#{competition} "
            + "ORDER BY date ASC LIMIT 1")
    Fixture getNextFixture(String competition);

    @Select("SELECT id as id, `date` as date, "
            + "homeTeam as homeTeam, awayTeam as awayTeam, "
            + "competition as competition, season as season, "
            + "result as result, stadium as stadium, "
            + "albumId as albumId, zimbioAlbumId as zimbioAlbumId, "
            + "gameweek as gameweek, city as city "
            + "FROM Fixtures "
            + "WHERE now() < DATE_ADD(`date`, INTERVAL 2 HOUR) "
            + "AND DATE_SUB(`date`, INTERVAL 1 HOUR) < now() "
            + "LIMIT 1")
    Fixture getCurrentFixture();
}

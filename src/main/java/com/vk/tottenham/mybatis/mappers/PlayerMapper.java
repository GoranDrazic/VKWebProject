package com.vk.tottenham.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.vk.tottenham.core.model.Player;

public interface PlayerMapper {

    @Select("SELECT id as id, name as name, "
            + "surname as surname, dateOfBirth as dateOfBirth, "
            + "position as position, dateJoined as dateJoined, "
            + "previousClubs as previousClubs, squadNumber as squadNumber, "
            + "instagram as instagram, russianNameNom as russianNameNom, "
            + "statsPhoto as statsPhoto "
            + "FROM Players "
            + "ORDER BY squadNumber ASC")
    List<Player> findAll();

    @Insert("INSERT INTO Players(id, name, surname, dateOfBirth,"
            + "position, dateJoined, previousClubs, squadNumber) VALUES"
            + "(#{id},#{name},#{surname}, #{dateOfBirth}, #{position},"
            + "#{dateJoined}, #{previousClubs}, #{squadNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id", flushCache = true, keyColumn = "id")
    void save(Player player);

    @Insert("UPDATE Players set name=#{name},surname=#{surname},"
            + "dateOfBirth=#{dateOfBirth},position=#{position},"
            + "dateJoined=#{dateJoined}, previousClubs=#{previousClubs},"
            + "squadNumber=#{squadNumber} "
            + "WHERE id=#{id}")
    void update(Player player);

    @Select("SELECT id as id, name as name, "
            + "surname as surname, dateOfBirth as dateOfBirth, "
            + "position as position, dateJoined as dateJoined, "
            + "previousClubs as previousClubs, squadNumber as squadNumber, "
            + "instagram as instagram, russianNameNom as russianNameNom, "
            + "statsPhoto as statsPhoto "
            + "FROM Players "
            + "WHERE DATE_FORMAT(dateOfBirth,'%m-%d') = DATE_FORMAT(DATE_ADD(NOW(),INTERVAL 0 DAY),'%m-%d') "
            + "AND (lastBDPost <> year(curdate()) OR lastBDPost IS NULL)")
    List<Player> findBirthdayBoys();

    @Deprecated
    @Select("SELECT DISTINCT id as id, name as name, "
            + "surname as surname, dateOfBirth as dateOfBirth, "
            + "position as position, dateJoined as dateJoined, "
            + "previousClubs as previousClubs, squadNumber as squadNumber, "
            + "instagram as instagram, russianNameNom as russianNameNom, "
            + "statsPhoto as statsPhoto "
            + "FROM Players "
            + "WHERE name=#{name} AND surname=#{surname}")
    Player findNameAndSurname(Player player);

    @Select("SELECT DISTINCT id as id, name as name, "
            + "surname as surname, dateOfBirth as dateOfBirth, "
            + "position as position, dateJoined as dateJoined, "
            + "previousClubs as previousClubs, squadNumber as squadNumber, "
            + "instagram as instagram, russianNameNom as russianNameNom, "
            + "statsPhoto as statsPhoto "
            + "FROM Players "
            + "WHERE id=#{id}")
    Player find(int id);

    @Select("UPDATE `Players` "
            + "SET lastBDPost = year(curdate()) "
            + "WHERE DATE_FORMAT(dateOfBirth,'%m-%d') = DATE_FORMAT(DATE_ADD(NOW(),INTERVAL 0 DAY),'%m-%d')")
    void updateLastDBPost();
}

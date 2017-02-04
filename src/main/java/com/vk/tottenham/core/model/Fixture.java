package com.vk.tottenham.core.model;

import java.util.Date;

public class Fixture {
    private int id;
    private Date date;
    private String homeTeam;
    private String awayTeam;
    private String competition;
    private String season;
    private String result;
    private String stadium;
    private String albumId;
    private String zimbioAlbumId;
    private int gameweek;
    private String city;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getZimbioAlbumId() {
        return zimbioAlbumId;
    }

    public void setZimbioAlbumId(String zimbioAlbumId) {
        this.zimbioAlbumId = zimbioAlbumId;
    }

    public int getGameweek() {
        return gameweek;
    }

    public void setGameweek(int gameweek) {
        this.gameweek = gameweek;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Game [id=" + id + ", date=" + date
                + ", homeTeam=" + homeTeam + ", awayTeam=" + awayTeam
                + ", competition=" + competition + ", season=" + season + ", result="
                + result + ", stadium=" + stadium + ", albumId=" + albumId
                + ", zimbioAlbumId=" + zimbioAlbumId + ", gameweek=" + gameweek
                + ", city=" + city + "]";
    }
}

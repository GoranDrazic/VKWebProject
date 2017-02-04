package com.vk.tottenham.footballapi.model;

public class Gameweek {
    private double id;
    private CompetitionSeason compSeason;
    private double gameweek;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public CompetitionSeason getCompSeason() {
        return compSeason;
    }

    public void setCompSeason(CompetitionSeason compSeason) {
        this.compSeason = compSeason;
    }

    public double getGameweek() {
        return gameweek;
    }

    public void setGameweek(double gameweek) {
        this.gameweek = gameweek;
    }
}

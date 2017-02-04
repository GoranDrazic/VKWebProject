package com.vk.tottenham.footballapi.model;

public class TeamScore {
    private Team team;
    private double score;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

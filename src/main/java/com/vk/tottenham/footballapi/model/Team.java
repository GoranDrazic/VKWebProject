package com.vk.tottenham.footballapi.model;

public class Team {
    private String name;
    private Club club;
    private String teamType;
    private String shortName;
    private double id;
    private AltIds altIds;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Club getClub() {
        return club;
    }
    public void setClub(Club club) {
        this.club = club;
    }
    public String getTeamType() {
        return teamType;
    }
    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public double getId() {
        return id;
    }
    public void setId(double id) {
        this.id = id;
    }
    public AltIds getAltIds() {
        return altIds;
    }
    public void setAltIds(AltIds altIds) {
        this.altIds = altIds;
    }
}

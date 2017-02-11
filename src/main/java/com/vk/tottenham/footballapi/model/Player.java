package com.vk.tottenham.footballapi.model;

public class Player {
    private int playerId;
    private Info info;
    private NationalTeam nationalTeam;
    private int height;
    private int weight;
    private String latestPosition;
    private int appearances;
    private int cleanSheets;
    private int saves;
    private int goalsConceded;
    private Date joinDate;
    private DateOfBirth birth;
    private String age;
    private Name name;
    private int id;
    private AltIds altIds;
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public Info getInfo() {
        return info;
    }
    public void setInfo(Info info) {
        this.info = info;
    }
    public NationalTeam getNationalTeam() {
        return nationalTeam;
    }
    public void setNationalTeam(NationalTeam nationalTeam) {
        this.nationalTeam = nationalTeam;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public String getLatestPosition() {
        return latestPosition;
    }
    public void setLatestPosition(String latestPosition) {
        this.latestPosition = latestPosition;
    }
    public int getAppearances() {
        return appearances;
    }
    public void setAppearances(int appearances) {
        this.appearances = appearances;
    }
    public int getCleanSheets() {
        return cleanSheets;
    }
    public void setCleanSheets(int cleanSheets) {
        this.cleanSheets = cleanSheets;
    }
    public int getSaves() {
        return saves;
    }
    public void setSaves(int saves) {
        this.saves = saves;
    }
    public int getGoalsConceded() {
        return goalsConceded;
    }
    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }
    public Date getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
    public DateOfBirth getBirth() {
        return birth;
    }
    public void setBirth(DateOfBirth birth) {
        this.birth = birth;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public Name getName() {
        return name;
    }
    public void setName(Name name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public AltIds getAltIds() {
        return altIds;
    }
    public void setAltIds(AltIds altIds) {
        this.altIds = altIds;
    }
}

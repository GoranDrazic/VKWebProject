package com.vk.tottenham.model;

import java.util.List;

public class TeamCurrentState {
    private int id;//: 1,
    private List<TeamFixture> currentEventFixture;/*": [
      {
        "is_home": true,
        "day": 26,
        "event_day": 1,
        "month": 12,
        "id": 171,
        "opponent": 19
      }
    ],*/
    private List<TeamFixture> nextEventFixture;/*": [
      {
        "is_home": true,
        "day": 1,
        "event_day": 3,
        "month": 1,
        "id": 181,
        "opponent": 5
      }
    ],*/
    private String name;//": "Arsenal",
    private int code;//": 3,
    private String shortName;//": "ARS",
    private boolean unavailable;//": false,
    private int strength;//": 4,
    private int position;//": 0,
    private int played;//": 0,
    private int win;//": 0,
    private int loss;//": 0,
    private int draw;//": 0,
    private int points;//": 0,
    private Object form;//": null,
    private String linkUrl;//": "",
    private int strengthOverallHome;//": 1280,
    private int strengthOverallAway;//": 1310,
    private int strengthAttackHome;//": 1300,
    private int strengthAttackAway;//": 1250,
    private int strengthDefenceHome;//": 1300,
    private int strengthDefenceAway;//": 1375,
    private int teamDivision;//": 1
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<TeamFixture> getCurrentEventFixture() {
        return currentEventFixture;
    }
    public void setCurrentEventFixture(List<TeamFixture> currentEventFixture) {
        this.currentEventFixture = currentEventFixture;
    }
    public List<TeamFixture> getNextEventFixture() {
        return nextEventFixture;
    }
    public void setNextEventFixture(List<TeamFixture> nextEventFixture) {
        this.nextEventFixture = nextEventFixture;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public boolean isUnavailable() {
        return unavailable;
    }
    public void setUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
    }
    public int getStrength() {
        return strength;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public int getPlayed() {
        return played;
    }
    public void setPlayed(int played) {
        this.played = played;
    }
    public int getWin() {
        return win;
    }
    public void setWin(int win) {
        this.win = win;
    }
    public int getLoss() {
        return loss;
    }
    public void setLoss(int loss) {
        this.loss = loss;
    }
    public int getDraw() {
        return draw;
    }
    public void setDraw(int draw) {
        this.draw = draw;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public Object getForm() {
        return form;
    }
    public void setForm(Object form) {
        this.form = form;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public int getStrengthOverallHome() {
        return strengthOverallHome;
    }
    public void setStrengthOverallHome(int strengthOverallHome) {
        this.strengthOverallHome = strengthOverallHome;
    }
    public int getStrengthOverallAway() {
        return strengthOverallAway;
    }
    public void setStrengthOverallAway(int strengthOverallAway) {
        this.strengthOverallAway = strengthOverallAway;
    }
    public int getStrengthAttackHome() {
        return strengthAttackHome;
    }
    public void setStrengthAttackHome(int strengthAttackHome) {
        this.strengthAttackHome = strengthAttackHome;
    }
    public int getStrengthAttackAway() {
        return strengthAttackAway;
    }
    public void setStrengthAttackAway(int strengthAttackAway) {
        this.strengthAttackAway = strengthAttackAway;
    }
    public int getStrengthDefenceHome() {
        return strengthDefenceHome;
    }
    public void setStrengthDefenceHome(int strengthDefenceHome) {
        this.strengthDefenceHome = strengthDefenceHome;
    }
    public int getStrengthDefenceAway() {
        return strengthDefenceAway;
    }
    public void setStrengthDefenceAway(int strengthDefenceAway) {
        this.strengthDefenceAway = strengthDefenceAway;
    }
    public int getTeamDivision() {
        return teamDivision;
    }
    public void setTeamDivision(int teamDivision) {
        this.teamDivision = teamDivision;
    }
    @Override
    public String toString() {
        return "TeamCurrentState [id=" + id + ", currentEventFixture="
                + currentEventFixture + ", nextEventFixture=" + nextEventFixture
                + ", name=" + name + ", code=" + code + ", shortName=" + shortName
                + ", unavailable=" + unavailable + ", strength=" + strength
                + ", position=" + position + ", played=" + played + ", win=" + win
                + ", loss=" + loss + ", draw=" + draw + ", points=" + points
                + ", form=" + form + ", linkUrl=" + linkUrl
                + ", strengthOverallHome=" + strengthOverallHome
                + ", strengthOverallAway=" + strengthOverallAway
                + ", strengthAttackHome=" + strengthAttackHome
                + ", strengthAttackAway=" + strengthAttackAway
                + ", strengthDefenceHome=" + strengthDefenceHome
                + ", strengthDefenceAway=" + strengthDefenceAway + ", teamDivision="
                + teamDivision + "]";
    }
}

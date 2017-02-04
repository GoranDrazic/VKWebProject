package com.vk.tottenham.model;

public class TeamFixture {
    private boolean isHome;//: true,
    private int day;//: 26,
    private int eventDay;//": 1,
    private int month;//: 12,
    private int id;//: 171,
    private int opponent;//: 19

    public boolean isHome() {
        return isHome;
    }
    public void setHome(boolean isHome) {
        this.isHome = isHome;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getEventDay() {
        return eventDay;
    }
    public void setEventDay(int eventDay) {
        this.eventDay = eventDay;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOpponent() {
        return opponent;
    }
    public void setOpponent(int opponent) {
        this.opponent = opponent;
    }
    @Override
    public String toString() {
        return "TeamFixture [isHome=" + isHome + ", day=" + day + ", eventDay="
                + eventDay + ", month=" + month + ", id=" + id + ", opponent="
                + opponent + "]";
    }
}

package com.vk.tottenham.footballapi.model;

public class LineupPlayer extends Player {
    private String matchPosition;
    private int matchShirtNumber;
    private boolean captain;

    public String getMatchPosition() {
        return matchPosition;
    }

    public void setMatchPosition(String matchPosition) {
        this.matchPosition = matchPosition;
    }

    public int getMatchShirtNumber() {
        return matchShirtNumber;
    }

    public void setMatchShirtNumber(int matchShirtNumber) {
        this.matchShirtNumber = matchShirtNumber;
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }
}
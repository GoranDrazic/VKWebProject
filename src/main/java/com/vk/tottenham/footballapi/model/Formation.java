package com.vk.tottenham.footballapi.model;

public class Formation {
    private String label;
    private int[][] players;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int[][] getPlayers() {
        return players;
    }

    public void setPlayers(int[][] players) {
        this.players = players;
    }
}

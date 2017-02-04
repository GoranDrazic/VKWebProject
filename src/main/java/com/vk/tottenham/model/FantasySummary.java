package com.vk.tottenham.model;

public class FantasySummary {
    private Standings standings;

    public Standings getStandings() {
        return standings;
    }

    public void setStandings(Standings standings) {
        this.standings = standings;
    }

    @Override
    public String toString() {
        return "FantasySummary [standings=" + standings + "]";
    }
}

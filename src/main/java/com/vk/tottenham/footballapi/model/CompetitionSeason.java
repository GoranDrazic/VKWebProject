package com.vk.tottenham.footballapi.model;

public class CompetitionSeason {
    private String label;
    private Competition competition;
    private double id;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CompetitionSeason [label=" + label + ", competition=" + competition
                + ", id=" + id + "]";
    }
}

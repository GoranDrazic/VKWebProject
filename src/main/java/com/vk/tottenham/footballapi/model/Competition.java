package com.vk.tottenham.footballapi.model;

import java.util.List;

public class Competition {
    private String abbreviation;
    private String description;
    private String level;
    private double id;
    private List<CompetitionSeason> compSeasons;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public List<CompetitionSeason> getCompSeasons() {
        return compSeasons;
    }

    public void setCompSeasons(List<CompetitionSeason> compSeasons) {
        this.compSeasons = compSeasons;
    }

    @Override
    public String toString() {
        return "Competition [abbreviation=" + abbreviation + ", description="
                + description + ", level=" + level + ", id=" + id + ", compSeasons="
                + compSeasons + "]";
    }
}

package com.vk.tottenham.footballapi.model;

import java.util.List;

public class TeamList {
    private int teamId;
    private List<LineupPlayer> lineup;
    private List<LineupPlayer> substitutes;
    private Formation formation;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public List<LineupPlayer> getLineup() {
        return lineup;
    }

    public void setLineup(List<LineupPlayer> lineup) {
        this.lineup = lineup;
    }

    public List<LineupPlayer> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<LineupPlayer> substitutes) {
        this.substitutes = substitutes;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }
}

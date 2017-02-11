package com.vk.tottenham.footballapi.model;

import java.util.List;

public class SquadResponse {
    private CompetitionSeason compSeason;
    private Team team;
    private List<Player> players;
    private List<Official> officials;
    public CompetitionSeason getCompSeason() {
        return compSeason;
    }
    public void setCompSeason(CompetitionSeason compSeason) {
        this.compSeason = compSeason;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public List<Official> getOfficials() {
        return officials;
    }
    public void setOfficials(List<Official> officials) {
        this.officials = officials;
    }
}

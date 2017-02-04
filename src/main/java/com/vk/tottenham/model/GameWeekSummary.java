package com.vk.tottenham.model;

import java.util.List;

public class GameWeekSummary {
    private List<TeamCurrentState> teams;
    private List<GameWeek> events;

    public List<GameWeek> getEvents() {
        return events;
    }

    public void setEvents(List<GameWeek> events) {
        this.events = events;
    }

    public List<TeamCurrentState> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamCurrentState> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "GameWeekSummary [teams=" + teams + ", events=" + events + "]";
    }
}

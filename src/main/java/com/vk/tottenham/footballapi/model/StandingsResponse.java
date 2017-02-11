package com.vk.tottenham.footballapi.model;

import java.util.List;

public class StandingsResponse {
    private CompetitionSeason compSeason;
    private boolean live;
    private boolean dynamicallyGenerated;
    private List<Table> tables;

    public CompetitionSeason getCompSeason() {
        return compSeason;
    }

    public void setCompSeason(CompetitionSeason compSeason) {
        this.compSeason = compSeason;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isDynamicallyGenerated() {
        return dynamicallyGenerated;
    }

    public void setDynamicallyGenerated(boolean dynamicallyGenerated) {
        this.dynamicallyGenerated = dynamicallyGenerated;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}

package com.vk.tottenham.footballapi.model;

public class StatsResponse {
    private String entity;
    private Stats stats;
    public String getEntity() {
        return entity;
    }
    public void setEntity(String entity) {
        this.entity = entity;
    }
    public Stats getStats() {
        return stats;
    }
    public void setStats(Stats stats) {
        this.stats = stats;
    }
}

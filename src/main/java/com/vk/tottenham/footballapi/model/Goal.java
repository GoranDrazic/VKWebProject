package com.vk.tottenham.footballapi.model;

public class Goal {
    private double personId;
    private Clock clock;
    private String phase;
    private String type;
    private String description;

    public double getPersonId() {
        return personId;
    }

    public void setPersonId(double personId) {
        this.personId = personId;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

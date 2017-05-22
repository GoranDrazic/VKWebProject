package com.vk.tottenham.footballapi.model;

public class Event {
    private int id;
    private int personId;
    private int teamId;
    private int assistId;
    private Clock clock;
    private String phase;
    private String type;// B(Y,R?)-Booked; S(ON,OFF) - Substitutions; PS - ?; G
                        // - Goal;
    private String description;
    private Time time;
    private Score score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getAssistId() {
        return assistId;
    }

    public void setAssistId(int assistId) {
        this.assistId = assistId;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
    
    public boolean isGoal() {
        return "G".equals(type);
    }
}

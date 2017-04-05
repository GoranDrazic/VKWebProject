package com.vk.tottenham.footballapi.model;

import java.util.List;

public class Fixture {
    private Gameweek gameweek;
    private Kickoff kickoff;
    private List<TeamScore> teams;
    private boolean replay;
    private Ground ground;
    private boolean neutralGround;
    private String status;
    private String phase;
    private String outcome;
    private Double attendance;
    private Clock clock;
    private List<Goal> goals;
    private double id;

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }

    public Kickoff getKickoff() {
        return kickoff;
    }

    public void setKickoff(Kickoff kickoff) {
        this.kickoff = kickoff;
    }

    public List<TeamScore> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamScore> teams) {
        this.teams = teams;
    }

    public boolean isReplay() {
        return replay;
    }

    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }

    public boolean isNeutralGround() {
        return neutralGround;
    }

    public void setNeutralGround(boolean neutralGround) {
        this.neutralGround = neutralGround;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Double getAttendance() {
        return attendance;
    }

    public void setAttendance(Double attendance) {
        this.attendance = attendance;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Fixture [gameweek=" + gameweek + ", kickoff=" + kickoff + ", teams="
                + teams + ", replay=" + replay + ", ground=" + ground
                + ", neutralGround=" + neutralGround + ", status=" + status
                + ", phase=" + phase + ", outcome=" + outcome + ", attendance="
                + attendance + ", clock=" + clock + ", goals=" + goals + ", id=" + id
                + "]";
    }

}

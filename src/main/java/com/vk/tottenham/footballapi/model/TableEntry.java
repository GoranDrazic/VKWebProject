package com.vk.tottenham.footballapi.model;

import java.util.List;

public class TableEntry {
    private Team team;
    private int position;
    private int startingPosition;
    private StatsCategory overall;
    private StatsCategory home;
    private StatsCategory away;
    private List<Annotation> annotations;
    private List<Fixture> form;
    private Fixture next;
    private Ground ground;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    public StatsCategory getOverall() {
        return overall;
    }

    public void setOverall(StatsCategory overall) {
        this.overall = overall;
    }

    public StatsCategory getHome() {
        return home;
    }

    public void setHome(StatsCategory home) {
        this.home = home;
    }

    public StatsCategory getAway() {
        return away;
    }

    public void setAway(StatsCategory away) {
        this.away = away;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<Fixture> getForm() {
        return form;
    }

    public void setForm(List<Fixture> form) {
        this.form = form;
    }

    public Fixture getNext() {
        return next;
    }

    public void setNext(Fixture next) {
        this.next = next;
    }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }
}

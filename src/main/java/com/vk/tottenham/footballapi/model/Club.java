package com.vk.tottenham.footballapi.model;

public class Club {
    private String name;
    private String abbr;
    private double id;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAbbr() {
        return abbr;
    }
    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
    public double getId() {
        return id;
    }
    public void setId(double id) {
        this.id = id;
    }
}

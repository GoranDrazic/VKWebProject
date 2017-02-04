package com.vk.tottenham.footballapi.model;

public class Clock {
    private double secs;
    private String label;// :"90 +4\u002700"

    public double getSecs() {
        return secs;
    }

    public void setSecs(double secs) {
        this.secs = secs;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

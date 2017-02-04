package com.vk.tottenham.footballapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Kickoff {
    private double completeness;
    private String millis;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE dd MMM yyyy, HH:mm z")
    private java.util.Date label;// "label":"Sat 13 Aug 2016, 15:00 BST",
    private double gmtOffset;

    public double getCompleteness() {
        return completeness;
    }

    public void setCompleteness(double completeness) {
        this.completeness = completeness;
    }

    public String getMillis() {
        return millis;
    }

    public void setMillis(String millis) {
        this.millis = millis;
    }

    public java.util.Date getLabel() {
        return label;
    }

    public void setLabel(java.util.Date label) {
        this.label = label;
    }

    public double getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(double gmtOffset) {
        this.gmtOffset = gmtOffset;
    }
}

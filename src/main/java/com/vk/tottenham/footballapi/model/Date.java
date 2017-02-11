package com.vk.tottenham.footballapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Date {
    private String millis;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="d MMMMM yyyy")
    private java.util.Date label;
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
}

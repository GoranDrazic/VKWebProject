package com.vk.tottenham.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Movement {
    UP("up"), DOWN("down"), SAME("same");
    private String value;

    private Movement(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

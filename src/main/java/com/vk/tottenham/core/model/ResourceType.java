package com.vk.tottenham.core.model;

public enum ResourceType {
    NEWS("news"), YOUTUBE("youtube"), TWITTER("twitter"), INSTAGRAM("instagram");
    private String value;

    private ResourceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

package com.vk.tottenham.core.model;

public enum NewsSource {
    OFFICIAL("official"), FAPL("fapl"), SPORT_EXPRESS("sport-express"), SPURS_RU("spurs_ru"), SPURS_ARMY("spurs_army");
    private String value;

    private NewsSource(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

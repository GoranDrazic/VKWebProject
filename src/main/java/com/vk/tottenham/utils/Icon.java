package com.vk.tottenham.utils;

public enum Icon {
    ARTICLE("&#128221;"), PHOTO("&#128247;");

    private String code;

    private Icon(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

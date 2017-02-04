package com.vk.tottenham.core.model;

import java.util.List;

public class Channel {
    private String title;
    private String link;
    private String description;
    private String language;
    private List<Article> item;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public List<Article> getItem() {
        return item;
    }
    public void setItem(List<Article> item) {
        this.item = item;
    }
}

package com.vk.tottenham.core.model;

import java.util.Date;

public class News {
    private String title;
    private Date published;
    private String summary;
    private String url;
    public News(String title, Date published, String summary, String url) {
        super();
        this.title = title;
        this.published = published;
        this.summary = summary;
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public Date getPublished() {
        return published;
    }
    public String getSummary() {
        return summary;
    }
    public String getUrl() {
        return url;
    }
}

package com.vk.tottenham.model.zimbio;

import java.util.List;

public class Summary {
    private String id;
    private String title;
    private int totalSlides;
    private List<Slide> slides;
    private String listUrl;
    private String success;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getTotalSlides() {
        return totalSlides;
    }
    public void setTotalSlides(int totalSlides) {
        this.totalSlides = totalSlides;
    }
    public List<Slide> getSlides() {
        return slides;
    }
    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }
    public String getListUrl() {
        return listUrl;
    }
    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    
}

package com.vk.tottenham.core.model;

import java.util.List;

import com.google.common.collect.Lists;

public class Twit {
    private String href;
    private String twitText;
    private String person;
    private List<String> photoElements = Lists.newArrayList();
    private int elapsedTime;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTwitText() {
        return twitText;
    }

    public void setTwitText(String twitText) {
        this.twitText = twitText;
    }

    public void appendTwitText(String additionalText) {
        this.twitText = twitText + "\n\n" + additionalText;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public List<String> getPhotoElements() {
        return photoElements;
    }

    public void setPhotoElements(List<String> photoElements) {
        this.photoElements = photoElements;
    }

    public void addPhotoElement(String photoElement) {
        this.photoElements.add(photoElement);
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public String toString() {
        return "Twit [href=" + href + ", twitText=" + twitText + ", person=" + person
                + ", photoElements=" + photoElements + ", elapsedTime=" + elapsedTime
                + "]";
    }
}

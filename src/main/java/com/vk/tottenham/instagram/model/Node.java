package com.vk.tottenham.instagram.model;

public class Node {
    private boolean isVideo;
    private String displaySrc;
    private String caption;

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public String getDisplaySrc() {
        return displaySrc;
    }

    public void setDisplaySrc(String displaySrc) {
        this.displaySrc = displaySrc;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

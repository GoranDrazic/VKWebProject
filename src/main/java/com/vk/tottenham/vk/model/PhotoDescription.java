package com.vk.tottenham.vk.model;

public class PhotoDescription {
    private String photoId;
    private int width;
    private int height;

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public PhotoDescription() {
        //
    }

    public PhotoDescription(String photoId) {
        this.photoId = photoId;
    }
}

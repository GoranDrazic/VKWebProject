package com.vk.tottenham.vk.model;

public class GetUploaderServerResponse {
    private String uploadUrl;
    private long albumId;
    private long userId;
    public String getUploadUrl() {
        return uploadUrl;
    }
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
    public long getAlbumId() {
        return albumId;
    }
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
}

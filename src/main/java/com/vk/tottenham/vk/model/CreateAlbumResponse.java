package com.vk.tottenham.vk.model;

import java.util.List;

public class CreateAlbumResponse {
    private String id;
    private int thumbId;
    private int ownerId;
    private String title;
    private String description;
    private long created;
    private long updated;
    private int size;
    private List<String> privacyView;
    private List<String> privacyComment;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getThumbId() {
        return thumbId;
    }
    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }
    public int getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getCreated() {
        return created;
    }
    public void setCreated(long created) {
        this.created = created;
    }
    public long getUpdated() {
        return updated;
    }
    public void setUpdated(long updated) {
        this.updated = updated;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public List<String> getPrivacyView() {
        return privacyView;
    }
    public void setPrivacyView(List<String> privacyView) {
        this.privacyView = privacyView;
    }
    public List<String> getPrivacyComment() {
        return privacyComment;
    }
    public void setPrivacyComment(List<String> privacyComment) {
        this.privacyComment = privacyComment;
    }
}

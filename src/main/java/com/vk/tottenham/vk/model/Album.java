package com.vk.tottenham.vk.model;

public class Album {
    private int id;
    private int thumbId;
    private long ownerId;
    private String title;
    private String description;
    private long created;
    private long updated;
    private int size;
    private int thumbIsLast;
    private int canUpload;
    private int uploadByAdminsOnly;
    private int commentsDisabled;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getThumbId() {
        return thumbId;
    }
    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }
    public long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(long ownerId) {
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
    public int getThumbIsLast() {
        return thumbIsLast;
    }
    public void setThumbIsLast(int thumbIsLast) {
        this.thumbIsLast = thumbIsLast;
    }
    public int getCanUpload() {
        return canUpload;
    }
    public void setCanUpload(int canUpload) {
        this.canUpload = canUpload;
    }
    public int getUploadByAdminsOnly() {
        return uploadByAdminsOnly;
    }
    public void setUploadByAdminsOnly(int uploadByAdminsOnly) {
        this.uploadByAdminsOnly = uploadByAdminsOnly;
    }
    public int getCommentsDisabled() {
        return commentsDisabled;
    }
    public void setCommentsDisabled(int commentsDisabled) {
        this.commentsDisabled = commentsDisabled;
    }
}

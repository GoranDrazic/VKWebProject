package com.vk.tottenham.vk.model;

public class GetPageResponse {
    private long id;
    private long groupId;
    private String title;
    private int currentUserCanEdit;
    private int currentUserCanEditAccess;
    private int whoCanView;
    private int whoCanEdit;
    private long edited;
    private long created;
    private int views;
    private long editorId;
    private long creatorId;
    private String source;
    private String viewUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurrentUserCanEdit() {
        return currentUserCanEdit;
    }

    public void setCurrentUserCanEdit(int currentUserCanEdit) {
        this.currentUserCanEdit = currentUserCanEdit;
    }

    public int getCurrentUserCanEditAccess() {
        return currentUserCanEditAccess;
    }

    public void setCurrentUserCanEditAccess(int currentUserCanEditAccess) {
        this.currentUserCanEditAccess = currentUserCanEditAccess;
    }

    public int getWhoCanView() {
        return whoCanView;
    }

    public void setWhoCanView(int whoCanView) {
        this.whoCanView = whoCanView;
    }

    public int getWhoCanEdit() {
        return whoCanEdit;
    }

    public void setWhoCanEdit(int whoCanEdit) {
        this.whoCanEdit = whoCanEdit;
    }

    public long getEdited() {
        return edited;
    }

    public void setEdited(long edited) {
        this.edited = edited;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public long getEditorId() {
        return editorId;
    }

    public void setEditorId(long editorId) {
        this.editorId = editorId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }
}

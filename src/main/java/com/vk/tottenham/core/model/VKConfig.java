package com.vk.tottenham.core.model;

public class VKConfig {
    private String groupId;
    private String mediaGroupId;
    private String albumId;
    private String chatId;

    public VKConfig(String groupId, String mediaGroupId, String albumId, String chatId) {
        super();
        this.groupId = groupId;
        this.mediaGroupId = mediaGroupId;
        this.albumId = albumId;
        this.chatId = chatId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMediaGroupId() {
        return mediaGroupId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getChatId() {
        return chatId;
    }
}

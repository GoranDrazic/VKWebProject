package com.vk.tottenham.model;

public class Slide {
    private String id;
    private String title;
    private String desc;
    private String shareDesc;
    private int index;
    private String route;
    private int displayIndex;
    private String imgUrl;
    //private Map<String, String> meta;
    private String originalImgUrl;
    //private Map<String, String> datastreamAttributes;
    private String url;
    private String nextAlbumSlideId;
    //private List<String> lookStack;
    private String regionThumbnail;
    private String regionImage;
    private String regionCaption;
    public String getRegionCaption() {
        return regionCaption;
    }
    public void setRegionCaption(String regionCaption) {
        this.regionCaption = regionCaption;
    }
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
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getShareDesc() {
        return shareDesc;
    }
    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
        this.route = route;
    }
    public int getDisplayIndex() {
        return displayIndex;
    }
    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getOriginalImgUrl() {
        return originalImgUrl;
    }
    public void setOriginalImgUrl(String originalImgUrl) {
        this.originalImgUrl = originalImgUrl;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getNextAlbumSlideId() {
        return nextAlbumSlideId;
    }
    public void setNextAlbumSlideId(String nextAlbumSlideId) {
        this.nextAlbumSlideId = nextAlbumSlideId;
    }
    public String getRegionThumbnail() {
        return regionThumbnail;
    }
    public void setRegionThumbnail(String regionThumbnail) {
        this.regionThumbnail = regionThumbnail;
    }
    public String getRegionImage() {
        return regionImage;
    }
    public void setRegionImage(String regionImage) {
        this.regionImage = regionImage;
    }
    @Override
    public String toString() {
        return "Slide [id=" + id + ", title=" + title + ", desc=" + desc
                + ", shareDesc=" + shareDesc + ", index=" + index + ", route="
                + route + ", displayIndex=" + displayIndex + ", imgUrl=" + imgUrl
                + ", originalImgUrl=" + originalImgUrl + ", url=" + url
                + ", nextAlbumSlideId=" + nextAlbumSlideId + ", regionThumbnail="
                + regionThumbnail + ", regionImage=" + regionImage
                + ", regionCaption=" + regionCaption + "]";
    }
}

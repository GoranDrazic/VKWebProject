package com.vk.tottenham.core.model;

import java.util.Date;
import com.vk.tottenham.jaxb.DateAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Article {
    private long id;
    private String guid;
    private String title;
    private String link;
    private Date originalPubDate;
    private String description;
    private String content;
    private String thumbnail;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getGuid() {
        return guid;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public Date getOriginalPubDate() {
        return originalPubDate;
    }
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlElement(name="pubDate")
    public void setOriginalPubDate(Date originalPubDate) {
        this.originalPubDate = originalPubDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    @Override
    public String toString() {
        return "Article [id=" + id + ", guid=" + guid + ", title=" + title
                + ", link=" + link + ", originalPubDate=" + originalPubDate
                + ", description=" + description + ", content=" + content
                + ", thumbnail=" + thumbnail + "]";
    }
    
    
}

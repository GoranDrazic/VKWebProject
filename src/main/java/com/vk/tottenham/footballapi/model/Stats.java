package com.vk.tottenham.footballapi.model;

import java.util.List;

public class Stats {
    private PageInfo pageInfo;
    private List<PlayerStatistics> content;
    public PageInfo getPageInfo() {
        return pageInfo;
    }
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
    public List<PlayerStatistics> getContent() {
        return content;
    }
    public void setContent(List<PlayerStatistics> content) {
        this.content = content;
    }  
}

package com.vk.tottenham.footballapi.model;

import java.util.List;

public class FixturesResponse {
    private PageInfo pageInfo;
    private List<Fixture> content;
    public PageInfo getPageInfo() {
        return pageInfo;
    }
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
    public List<Fixture> getContent() {
        return content;
    }
    public void setContent(List<Fixture> content) {
        this.content = content;
    }
}

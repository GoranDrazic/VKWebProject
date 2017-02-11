package com.vk.tottenham.footballapi.model;

import java.util.List;

public class CompetitionsResponse {
    private PageInfo pageInfo;
    private List<Competition> content;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Competition> getContent() {
        return content;
    }

    public void setContent(List<Competition> content) {
        this.content = content;
    }
}

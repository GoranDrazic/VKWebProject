package com.vk.tottenham.core.model;

import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.template.PageType;

public class Page {
    private String season;
    private CompetitionName competition;
    private PageType pageType;
    private String pageLink;

    public Page() {
        super();
    }

    public Page(String season, CompetitionName competition, PageType pageType) {
        super();
        this.season = season;
        this.competition = competition;
        this.pageType = pageType;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public CompetitionName getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionName competition) {
        this.competition = competition;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public int compareTo(Page page) {
        if (!this.getSeason().equals(page.getSeason())) {
            return this.getSeason().compareTo(page.getSeason());
        } 
        return page.getPageType().order() - this.getPageType().order();
    }
}

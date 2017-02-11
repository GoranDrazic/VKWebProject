package com.vk.tottenham.template;

import java.util.Map;

import com.vk.tottenham.core.model.Page;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.FixturesResponse;
import com.vk.tottenham.footballapi.model.StandingsResponse;
import com.vk.tottenham.footballapi.model.StatsResponse;

public class PageContext {
    private StatsResponse assists;
    private StatsResponse goals;
    private FixturesResponse fixtures;
    private StandingsResponse standings;
    private CompetitionName competition;
    private Map<PageType, Page> links;
    private PageType pageType;
    private Map<Year, Page> footerPages;

    public StatsResponse getAssists() {
        return assists;
    }

    public StatsResponse getGoals() {
        return goals;
    }

    public FixturesResponse getFixtures() {
        return fixtures;
    }

    public StandingsResponse getStandings() {
        return standings;
    }

    public Map<PageType, Page> getLinks() {
        return links;
    }

    public Map<Year, Page> getFooterPages() {
        return footerPages;
    }

    public CompetitionName getCompetition() {
        return competition;
    }

    public PageType getPageType() {
        return pageType;
    }

    public PageContext(StatsResponse assists, StatsResponse goals,
            FixturesResponse fixtures, StandingsResponse standings,
            Map<PageType, Page> links, Map<Year, Page> footerPages, 
            CompetitionName competition, PageType pageType) {
        super();
        this.assists = assists;
        this.goals = goals;
        this.fixtures = fixtures;
        this.standings = standings;
        this.links = links;
        this.footerPages = footerPages;
        this.competition = competition;
        this.pageType = pageType;
    }
}
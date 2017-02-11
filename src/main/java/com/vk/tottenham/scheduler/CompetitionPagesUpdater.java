package com.vk.tottenham.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.vk.tottenham.core.model.Page;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.footballapi.FootballApiGateway;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.CompetitionsResponse;
import com.vk.tottenham.footballapi.model.FixturesResponse;
import com.vk.tottenham.footballapi.model.StandingsResponse;
import com.vk.tottenham.footballapi.model.StatsResponse;
import com.vk.tottenham.mybatis.service.DictionaryService;
import com.vk.tottenham.mybatis.service.PageService;
import com.vk.tottenham.template.PageContext;
import com.vk.tottenham.template.PageType;
import com.vk.tottenham.template.TemplateBase;
import com.vk.tottenham.template.Year;
import com.vk.tottenham.utils.CompSeasonUtil;
import com.vk.tottenham.vk.model.GetPageResponse;

public class CompetitionPagesUpdater extends SchedulerBase implements ApplicationContextAware {

    @Autowired
    @Qualifier("footballApiGateway")
    private FootballApiGateway footballApiGateway;

    @Autowired
    @Qualifier("pageService")
    private PageService pageService;

    @Autowired
    @Qualifier("dictionaryService")
    private DictionaryService dictionaryService;

    private ApplicationContext applicationContext;

    @Override
    public void execute() {
        CompetitionsResponse competitionsResponse = footballApiGateway.getCompetitions();
        for (CompetitionName competition : CompetitionName.values()) {
            List<Page> pages = pageService.findAll(); 
            String compSeason = CompSeasonUtil.getCompSeason(competitionsResponse, competition);
            if (compSeason == null) return;

            Map<PageType, Page> links = getLinks(pages, competition);

            StatsResponse assistsResponse = footballApiGateway.getAssistStats(competition.comp(), compSeason);
            StatsResponse goalResponse = footballApiGateway.getGoalStats(competition.comp(), compSeason);
            FixturesResponse fixturesResponse = footballApiGateway.getFixtures(competition.comp(), compSeason);
            StandingsResponse standingsResponse = footballApiGateway.getStandings(competition.comp(), compSeason);
            for (PageType pageType : competition.supportedPages()) {
                Map<Year, Page> footerPages = getFooterPages(pages, competition, pageType);
                PageContext context = new PageContext(assistsResponse, goalResponse,
                        fixturesResponse, standingsResponse, links, footerPages, competition, pageType);
                try {
                    TemplateBase template = (TemplateBase) applicationContext.getBean(pageType.templateClassName());
                    if (template.isUpdateNeeded(context)) {
                        String pageContents = template.build(context);
                        Page page = new Page(CompSeasonUtil.getSeasonFull(), competition, pageType);
                        Page existingPage = pageService.findById(page);
                        if (existingPage == null) {
                            List<Page> thisYearPages = pageService
                                    .findByYearAndCompetition(CompSeasonUtil.getSeasonFull(), competition);
                            if (shouldBecomeFirstPage(page, thisYearPages)) {
                                String firstPageId = getPageIdByPageLink(competition.firstPage());
                                thisYearPages.size();
                                Page oldPageObject = pageService.findByPageLink(competition.firstPage());
                                GetPageResponse oldPage = vkGateway.getPage(getGroupId(), firstPageId);
                                //save old page under temporary name
                                int newOldPageId = vkGateway.savePage(getGroupId(), null, oldPage.getTitle() + UUID.randomUUID().toString(), oldPage.getSource());
                                oldPageObject.setPageLink(getPageLinkByPageId(String.valueOf(newOldPageId)));
                                pageService.update(oldPageObject);
                                //save new page under old id(first page id)
                                vkGateway.savePage(getGroupId(), firstPageId, CompSeasonUtil.getSeasonFull() + ". " 
                                        + dictionaryService.translateInRussianNom(competition.value()) + ". "
                                        + dictionaryService.translateInRussianNom(pageType.caption()), pageContents);
                                Page newPage = new Page(CompSeasonUtil.getSeasonFull(), competition, pageType);
                                newPage.setPageLink(competition.firstPage());
                                pageService.save(newPage);
                                //rename old page. set beautiful name
                                vkGateway.savePage(getGroupId(), String.valueOf(newOldPageId), oldPage.getTitle(), oldPage.getSource());
                                //save old page with new id
                                //save new page with old id
                            } else {
                                int pageId = vkGateway.savePage(getGroupId(), null, 
                                        CompSeasonUtil.getSeasonFull() + ". " 
                                        + dictionaryService.translateInRussianNom(competition.value()) + ". "
                                        + dictionaryService.translateInRussianNom(pageType.caption()), 
                                        pageContents);
                                page.setPageLink("page-" + getGroupId() + "_" + pageId);
                                pageService.save(page);
                            }
                        } else {
                            String pageId = getPageIdByPageLink(existingPage.getPageLink());
                            String currentContents = vkGateway.getPage(getGroupId(), pageId).getSource();
                            if (!pageContents.trim().equals(currentContents)) {
                                vkGateway.savePage(getGroupId(), pageId, null,
                                        pageContents);
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new VkException("Exception creating a page.", e);
                }
            }
        }
    }

    private boolean shouldBecomeFirstPage(Page page, List<Page> thisYearPages) {
        if (thisYearPages.size() == 0) {
            return true;
        }
        for (Page thisYearPage : thisYearPages) {
            if (page.compareTo(thisYearPage) > 0) {
                return false;
            }
        }
        return true;
    }

    private Map<Year, Page> getFooterPages(List<Page> pages,
            CompetitionName competition, PageType pageType) {
        Map<Year, Page> result = new HashMap<>();
        for (Page page : pages) {
            if (page.getCompetition() == competition &&
                    page.getPageType() == pageType) {
                result.put(Year.fromFullYear(page.getSeason()), page);
            }
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Map<PageType, Page> getLinks(List<Page> pages, CompetitionName competition) {
        Map<PageType, Page> result = new HashMap<>();
        String season = CompSeasonUtil.getSeasonStart();
        for (Page page : pages) {
            if (page.getCompetition() == competition && page.getSeason().startsWith(season)) {
                result.put(page.getPageType(), page);
            }
        }
        return result;
    }

    private String getPageLinkByPageId(String pageId) {
        return "page-" + getGroupId() + "_" + pageId;
    }

    private String getPageIdByPageLink(String pageLink) {
        int dashIndex = pageLink.indexOf("_");
        return pageLink.substring(dashIndex + 1);
    }
}
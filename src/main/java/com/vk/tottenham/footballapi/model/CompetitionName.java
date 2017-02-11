package com.vk.tottenham.footballapi.model;

import static com.vk.tottenham.template.PageType.*;

import com.vk.tottenham.template.PageType;

public enum CompetitionName {
    PREMIER_LEAGUE("Premier League", "1", new PageType[]{CALENDAR, TABLE, STATS}, "photo-15474997_399980319", "photo-15474997_399975946", "page-15474997_50872758"), 
    FA_CUP("FA Cup", "4", new PageType[]{CALENDAR, STATS}, "photo-15474997_399980320", "photo-15474997_399975947", "page-15474997_11056066"), 
    EFL_CUP("EFL Cup", "5", new PageType[]{CALENDAR, STATS}, "photo-15474997_399980325", "photo-15474997_399975949", "page-15474997_50875060"),
    UEFA_CHAMPIONS_LEAGUE("UEFA Champions League", "2", new PageType[]{CALENDAR, /*TODO:PLAY_OFFS, */GROUP_STAGE, QUALIFICATION, STATS}, "photo-15474997_399980329", "photo-15474997_399975952", "page-15474997_50875305"), 
    UEFA_EUROPA_LEAGUE("UEFA Europa League", "3", new PageType[]{CALENDAR, /*TODO:PLAY_OFFS, */GROUP_STAGE, QUALIFICATION, STATS}, "photo-15474997_399980333", "photo-15474997_399975959", "page-15474997_50875307");
    
    private String value;
    private String comp;
    private PageType[] supportedPages;
    private String activeIcon;
    private String inactiveIcon;
    private String firstPage;

    private CompetitionName(String value, String comp, PageType[] supportedPages,
            String activeIcon, String inactiveIcon, String firstPage) {
        this.value = value;
        this.comp = comp;
        this.supportedPages = supportedPages;
        this.activeIcon = activeIcon;
        this.inactiveIcon = inactiveIcon;
        this.firstPage = firstPage;
    }

    public String value() {
        return value;
    }

    public String comp() {
        return comp;
    }

    public PageType[] supportedPages() {
        return supportedPages;
    }

    public String activeIcon() {
        return activeIcon;
    }

    public String inactiveIcon() {
        return inactiveIcon;
    }

    public String firstPage() {
        return firstPage;
    }
}
package com.vk.tottenham.footballapi.model;

public enum CompetitionName {
    PREMIER_LEAGUE("Premier League", "1", "54", "com/vk/tottenham/contents/statsContents.txt", "10384325"), 
    FA_CUP("FA Cup", "4", "71", "com/vk/tottenham/contents/statsContentsFACup.txt", "52543642"), 
    EFL_CUP("EFL Cup", "5", "56", "com/vk/tottenham/contents/statsContentsEFLCup.txt", "52543702"),
    UEFA_CHAMPIONS_LEAGUE("UEFA Champions League", "2", "66", "", ""), 
    UEFA_EUROPA_LEAGUE("UEFA Europa League", "3", "70", "", "");
    
    private String value;
    private String comp;
    private String compSeason;
    private String statsTemplate;
    private String statsPage;

    private CompetitionName(String value, String comp, String compSeason, String statsTemplate, String statsPage) {
        this.value = value;
        this.comp = comp;
        this.compSeason = compSeason;
        this.statsTemplate = statsTemplate;
        this.statsPage = statsPage;
    }

    public String value() {
        return value;
    }

    public String comp() {
        return comp;
    }

    public String compSeason() {
        return compSeason;
    }

    public String statsTemplate() {
        return statsTemplate;
    }

    public String statsPage() {
        return statsPage;
    }
}

package com.vk.tottenham.footballapi.model;

public enum CompetitionName {
    PREMIER_LEAGUE("Premier League", "1", "54"), FA_CUP("FA Cup", "4", "71"), EFL_CUP("EFL Cup", "5", "56"),
    UEFA_CHAMPIONS_LEAGUE("UEFA Champions League", "2", "66"), UEFA_EUROPA_LEAGUE("UEFA Europa League", "3", "70");
    
    private String value;
    private String comp;
    private String compSeason;

    private CompetitionName(String value, String comp, String compSeason) {
        this.value = value;
        this.comp = comp;
        this.compSeason = compSeason;
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
}

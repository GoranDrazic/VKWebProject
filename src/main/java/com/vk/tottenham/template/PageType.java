package com.vk.tottenham.template;

public enum PageType {
    CALENDAR("calendarTemplate", 0, "Calendar"), TABLE("tableTemplate", 1, "Standings"), 
    PLAY_OFFS("playOffsTemplate", 2, "Play-off"), GROUP_STAGE("groupStageTemplate", 3, "Group Stage"), 
    QUALIFICATION("qualificationTemplate", 4, "Qualification"), STATS("statsTemplate", 5, "Stats");

    private String templateClassName;
    private int order;
    private String caption;

    private PageType(String templateClassName, int order, String caption) {
        this.templateClassName = templateClassName;
        this.order = order;
        this.caption = caption;
    }

    public String templateClassName() {
        return templateClassName;
    }

    public int order() {
        return order;
    }

    public String caption() {
        return caption;
    }
}

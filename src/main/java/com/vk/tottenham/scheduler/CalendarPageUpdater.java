package com.vk.tottenham.scheduler;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.vk.tottenham.footballapi.FootballApiGateway;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.Fixture;
import com.vk.tottenham.footballapi.model.FixturesResponse;
import com.vk.tottenham.mybatis.service.DictionaryService;
import com.vk.tottenham.mybatis.service.TeamService;

public class CalendarPageUpdater extends SchedulerBase {

    @Autowired
    @Qualifier("footballApiGateway")
    private FootballApiGateway footballApiGateway;

    @Autowired
    @Qualifier("dictionaryService")
    private DictionaryService dictionaryService;

    @Autowired
    @Qualifier("teamService")
    private TeamService teamService;

    public static SimpleDateFormat monthFormatter = new SimpleDateFormat(
            "MMMMM", new Locale("ru"));

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "dd.MM.yy", Locale.US);

    private static final String MONTH_START_TEMPLATE = "{{Hider|##show## <center>##month##</center>\n{|\n";
    private static final String MONTH_END_TEMPLATE = "|}\n}}\n";
    
    private static final String AWAY_FIXTURE_TEMPLATE = "|-\n|<center>##date##</center>||<center>[[##smallIcon##|35px;noborder;nolink|##competitionName##]]</center>||<center>«##homeTeam##» - '''«##awayTeam##»'''</center>||<center>##homeScore##:##awayScore##</center>\n";
    private static final String HOME_FIXTURE_TEMPLATE = "|-\n|<center>##date##</center>||<center>[[##smallIcon##|35px;noborder;nolink|##competitionName##]]</center>||<center>'''«##homeTeam##»''' - «##awayTeam##»</center>||<center>##homeScore##:##awayScore##</center>\n";

    private static final String UNKNOWN_DATE = "ДАТА НЕОПРЕДЕЛЕНА";
    private static final String POSTPONED = "Перенесён";

    private static final long FIFTEEN_DAYS = 15 * 24 * 60 * 60 * 1000; 

    private static final String PAGE_ID = "11146623";

    @Override
    public void execute() {
        //Assume that this guy returns fixtures in chronological order. Not scheduled games go first. 
        FixturesResponse fixtures = footballApiGateway.getAllFixtures();
        Map<String, LinkedList<Fixture>> monthToFixture = new LinkedHashMap<>(); 
        for (Fixture fixture : fixtures.getContent()) {
            String fixtureMonth = UNKNOWN_DATE;
            Date date = fixture.getKickoff().getLabel();
            if (date != null) {
                fixtureMonth = monthFormatter.format(date).toUpperCase();
            }
            if (monthToFixture.get(fixtureMonth) == null) {
                monthToFixture.put(fixtureMonth, new LinkedList<>());
            }
            monthToFixture.get(fixtureMonth).add(fixture);
        }

        LinkedList<Fixture> notScheduled = monthToFixture.remove(UNKNOWN_DATE);
        monthToFixture.put(UNKNOWN_DATE, notScheduled);

        StringBuilder builder = new StringBuilder();
        for (Entry<String, LinkedList<Fixture>> monthOfFixtures : monthToFixture.entrySet()) {
            Fixture lastFixture = monthOfFixtures.getValue().getLast();
            Date lastFixtureDate = lastFixture.getKickoff().getLabel() != null ? lastFixture.getKickoff().getLabel() : null;
            String show = lastFixtureDate == null || lastFixtureDate.after(
                    new Date(System.currentTimeMillis() + FIFTEEN_DAYS)) ? "show|" : "";
            builder.append(MONTH_START_TEMPLATE.replaceAll("##month##", monthOfFixtures.getKey()).replace("##show##", show));
            for (Fixture fixture : monthOfFixtures.getValue()) {
                Date label = fixture.getKickoff().getLabel();
                String fixtureDate = label != null ? dateFormatter.format(label) : POSTPONED;
                String homeScore = label == null || label.after(new Date()) ? "_"
                        : Integer.valueOf((int) fixture.getTeams().get(0).getScore()).toString();
                String awayScore = label == null || label.after(new Date()) ? "_"
                        : Integer.valueOf((int) fixture.getTeams().get(1).getScore()).toString();
                String competitionName = fixture.getGameweek().getCompSeason().getCompetition().getDescription();
                String competitionNameInRussian = dictionaryService.translateInRussianNom(competitionName);
                String fixtureTemplate = "Tottenham Hotspur"
                        .equals(fixture.getTeams().get(0).getTeam().getName())
                                ? HOME_FIXTURE_TEMPLATE : AWAY_FIXTURE_TEMPLATE;
                fixtureTemplate = fixtureTemplate.replaceAll("##homeTeam##", teamService.getRussianTeamNameInNominativeByFapiName(fixture.getTeams().get(0).getTeam().getName()))
                    .replaceAll("##awayTeam##", teamService.getRussianTeamNameInNominativeByFapiName(fixture.getTeams().get(1).getTeam().getName()))
                    .replaceAll("##homeScore##", homeScore)
                    .replaceAll("##awayScore##", awayScore)
                    .replaceAll("##date##", fixtureDate)
                    .replaceAll("##smallIcon##", CompetitionName.getByName(competitionName).smallIcon())
                    .replaceAll("##competitionName##", competitionNameInRussian);
                builder.append(fixtureTemplate);
            }
            builder.append(MONTH_END_TEMPLATE);
        }
        URL url = Resources.getResource(
                "com/vk/tottenham/contents/calendarPageContent.txt");
        String pageContents = "";
        try {
            pageContents = Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {}
        pageContents = pageContents.replace("##content##",
                builder.toString());

        String currentContents = vkGateway.getPage(getGroupId(), PAGE_ID).getSource();
        if (!pageContents.trim().equals(currentContents)) {
            vkGateway.savePage(getGroupId(), PAGE_ID, null,
                    pageContents);
        }
    }
}

package com.vk.tottenham.template;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.Fixture;
import com.vk.tottenham.mybatis.service.DictionaryService;
import com.vk.tottenham.mybatis.service.TeamService;

@Component("calendarTemplate")
public class CalendarTemplate extends TemplateBase {
    
    private static final String NEXT_FIXTURE_CAPTION = "Следующий матч";
    private static final String PREV_FIXTURE_CAPTION = "Предыдущий матч";

    private static final String FIXTURE_TEMPLATE = "\n{|\n|-\n! <center><gray>##fixtureCaption##:</gray></center>\n|}\n{|\n##fixture##|}\n";
    
    private static final String NEXT_FIXTURE = "|-\n| <center>[[##homeTeamPhotoId##|70px;noborder;nolink|"
            + "«##homeTeamNameInRussian##»]]</center>\n| <center>"
            + "##tournament##. ##gameWeek##-ый тур<br/>##fixtureDate##<br/>"
            + "Начало матча в ##fixtureTime## по Москве<br/>##homeCity##. "
            + "«##Stadium##»<br/><b>«##homeTeamNameInRussian##» -:- "
            + "«##awayTeamNameInRussian##»</b></center>\n| <center>"
            + "[[##awayTeamPhotoId##|70px;noborder;nolink|«##awayTeamNameInRussian##»]] </center>\n";
    
    private static final String PREVIOUS_FIXTURE = "|-\n| <center>[[##homeTeamPhotoId##|70px;noborder;nolink|"
            + "«##homeTeamNameInRussian##»]]</center>\n| <center>"
            + "##tournament##. ##gameWeek##-ый тур<br/>##fixtureDate##<br/>##homeCity##. "
            + "«##Stadium##»<br/><b>«##homeTeamNameInRussian##» ##score## "
            + "«##awayTeamNameInRussian##»</b></center>\n| <center>"
            + "[[##awayTeamPhotoId##|70px;noborder;nolink|«##awayTeamNameInRussian##»]]</center>\n";

    private static final String DATE_FORMAT = "d MMMMM, EEEEE";

    private static final String TIME_FORMAT = "HH:mm";

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat(
            TIME_FORMAT, new Locale("ru"));

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
            DATE_FORMAT, new Locale("ru"));

    @Autowired
    @Qualifier("teamService")
    private TeamService teamService;

    @Override
    public boolean isUpdateNeeded(PageContext context) {
        return context.getFixtures().getContent().size() > 0;
    }

    @Override
    public String buildContent(PageContext context) {
        StringBuilder builer = new StringBuilder();

        LinkedHashMap<String, Fixture> fixturesToDisplay = getFixturesToDisplay(
                context.getFixtures().getContent(), context.getCompetition());
        for (Entry<String, Fixture> fixtureEntry : fixturesToDisplay.entrySet()) {
            if (fixtureEntry.getValue().getOutcome() == null) {
                Fixture next = fixtureEntry.getValue();
                String caption = fixtureEntry.getKey();
                String nextFragment = new String(FIXTURE_TEMPLATE);
                nextFragment = nextFragment.replace("##fixtureCaption##", caption);
                String nextFixture = new String(NEXT_FIXTURE);
                nextFixture = nextFixture.replaceAll("##homeTeamPhotoId##", teamService.getStandingsIconByFapiName(next.getTeams().get(0).getTeam().getName()))
                        .replaceAll("##homeTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(next.getTeams().get(0).getTeam().getName()))
                        .replaceAll("##tournament##", dictionaryService.translateInRussianNom(next.getGameweek().getCompSeason().getCompetition().getDescription()))
                        .replaceAll("##gameWeek##", String.valueOf((int)next.getGameweek().getGameweek()))
                        .replaceAll("##fixtureDate##", dateFormatter.format(next.getKickoff().getLabel()))
                        .replaceAll("##fixtureTime##", timeFormatter.format(next.getKickoff().getLabel()))
                        .replaceAll("##homeCity##", dictionaryService.translateInRussianNom(next.getGround().getCity()))
                        .replaceAll("##Stadium##", dictionaryService.translateInRussianNom(next.getGround().getName()))
                        .replaceAll("##awayTeamPhotoId##", teamService.getStandingsIconByFapiName(next.getTeams().get(1).getTeam().getName()))
                        .replaceAll("##awayTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(next.getTeams().get(1).getTeam().getName()));
                nextFragment = nextFragment.replace("##fixture##", nextFixture);
                builer.append(nextFragment);
            } else {
                Fixture previous = fixtureEntry.getValue();
                String caption = fixtureEntry.getKey();
                String prevFragment = new String(FIXTURE_TEMPLATE);
                prevFragment = prevFragment.replace("##fixtureCaption##", caption);
                String prevFixture = new String(PREVIOUS_FIXTURE);
                prevFixture = prevFixture.replaceAll("##homeTeamPhotoId##", teamService.getStandingsIconByFapiName(previous.getTeams().get(0).getTeam().getName()))
                        .replaceAll("##homeTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(previous.getTeams().get(0).getTeam().getName()))
                        .replaceAll("##tournament##", dictionaryService.translateInRussianNom(previous.getGameweek().getCompSeason().getCompetition().getDescription()))
                        .replaceAll("##gameWeek##", String.valueOf((int)previous.getGameweek().getGameweek()))
                        .replaceAll("##fixtureDate##", dateFormatter.format(previous.getKickoff().getLabel()))
                        .replaceAll("##homeCity##", dictionaryService.translateInRussianNom(previous.getGround().getCity()))
                        .replaceAll("##Stadium##", dictionaryService.translateInRussianNom(previous.getGround().getName()))
                        .replaceAll("##awayTeamPhotoId##", teamService.getStandingsIconByFapiName(previous.getTeams().get(1).getTeam().getName()))
                        .replaceAll("##awayTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(previous.getTeams().get(1).getTeam().getName()))
                        .replaceAll("##score##", (int)previous.getTeams().get(0).getScore() + ":" + (int)previous.getTeams().get(1).getScore());
                prevFragment = prevFragment.replace("##fixture##", prevFixture);
                builer.append(prevFragment);
            }
        }
        return builer.toString();
    }

    @Override
    protected boolean shouldAddFooter(PageContext context) {
        return context.getCompetition() == CompetitionName.EFL_CUP || 
                context.getCompetition() == CompetitionName.FA_CUP;
    }

    private LinkedHashMap<String, Fixture> getFixturesToDisplay(
            List<Fixture> fixtures, CompetitionName competition) {
        LinkedHashMap<String, Fixture> result = new LinkedHashMap<>();
        if (competition == CompetitionName.EFL_CUP || competition == CompetitionName.FA_CUP) {
            for (Fixture fixture : fixtures) {
                String caption = "N-й раунд".replace("N",
                        String.valueOf((int) fixture.getGameweek().getGameweek()));
                result.put(caption, fixture);
            }
        } else {
            Date now = new Date();
            Fixture next = null;
            Fixture previous = null;
            for (Fixture fixture : fixtures) {
                Date fixtureDate = fixture.getKickoff().getLabel();
                if (fixtureDate != null && fixtureDate.after(now)) {
                    //next
                    if (next == null || next.getKickoff().getLabel().after(fixtureDate)) {
                        next = fixture;
                    }
                } else {
                    //previous
                    if (fixtureDate != null && (previous == null || previous.getKickoff().getLabel().before(fixtureDate))) {
                        previous = fixture;
                    }
                }
            }
            if (next != null) {
                String nextFragment = NEXT_FIXTURE_CAPTION;
                result.put(nextFragment, next);
            }
            if (previous != null) {
                String prevFragment = PREV_FIXTURE_CAPTION;
                result.put(prevFragment, previous);
            }
        }
        return result;
    }
}

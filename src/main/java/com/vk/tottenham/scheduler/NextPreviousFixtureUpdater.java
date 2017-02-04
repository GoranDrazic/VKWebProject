package com.vk.tottenham.scheduler;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.vk.tottenham.core.model.Fixture;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.mybatis.service.DictionaryService;
import com.vk.tottenham.mybatis.service.FixtureService;
import com.vk.tottenham.mybatis.service.TeamService;

public class NextPreviousFixtureUpdater extends SchedulerBase {
    
    private static final String DATE_FORMAT = "d MMMMM, EEEEE";

    private static final String TIME_FORMAT = "HH:mm";

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat(
            TIME_FORMAT, new Locale("ru"));

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
            DATE_FORMAT, new Locale("ru"));

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

    @Autowired
    @Qualifier("fixtureService")
    private FixtureService fixtureService;

    @Autowired
    @Qualifier("teamService")
    private TeamService teamService;

    @Autowired
    @Qualifier("dictionaryService")
    private DictionaryService dictionaryService;

    @Override
    public void execute() {
        try {
            URL url = Resources.getResource(
                    "com/vk/tottenham/contents/nextPreviousFixtureContent.txt");
            String pageContents = Resources.toString(url, Charsets.UTF_8);
            Fixture next = fixtureService.getNextFixture(CompetitionName.PREMIER_LEAGUE.value());
            String nextFixture = next != null ? new String(NEXT_FIXTURE) : "";
            if (next != null) {
                nextFixture = nextFixture.replaceAll("##homeTeamPhotoId##", teamService.getStandingsIconByFapiName(next.getHomeTeam()))
                        .replaceAll("##homeTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(next.getHomeTeam()))
                        .replaceAll("##tournament##", dictionaryService.translateInRussianNom(next.getCompetition()))
                        .replaceAll("##gameWeek##", String.valueOf(next.getGameweek()))
                        .replaceAll("##fixtureDate##", dateFormatter.format(next.getDate()))
                        .replaceAll("##fixtureTime##", timeFormatter.format(next.getDate()))
                        .replaceAll("##homeCity##", dictionaryService.translateInRussianNom(next.getCity()))
                        .replaceAll("##Stadium##", dictionaryService.translateInRussianNom(next.getStadium()))
                        .replaceAll("##awayTeamPhotoId##", teamService.getStandingsIconByFapiName(next.getAwayTeam()))
                        .replaceAll("##awayTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(next.getAwayTeam()));
            }
            pageContents = pageContents.replace("##nextFixture##", nextFixture);
            Fixture previous = fixtureService.getPreviousFixture(CompetitionName.PREMIER_LEAGUE.value());
            String previousFixture = previous != null ? new String(PREVIOUS_FIXTURE) : "";
            if (previous != null) {
                previousFixture = previousFixture.replaceAll("##homeTeamPhotoId##", teamService.getStandingsIconByFapiName(previous.getHomeTeam()))
                        .replaceAll("##homeTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(previous.getHomeTeam()))
                        .replaceAll("##tournament##", dictionaryService.translateInRussianNom(previous.getCompetition()))
                        .replaceAll("##gameWeek##", String.valueOf(previous.getGameweek()))
                        .replaceAll("##fixtureDate##", dateFormatter.format(previous.getDate()))
                        .replaceAll("##homeCity##", dictionaryService.translateInRussianNom(previous.getCity()))
                        .replaceAll("##Stadium##", dictionaryService.translateInRussianNom(previous.getStadium()))
                        .replaceAll("##awayTeamPhotoId##", teamService.getStandingsIconByFapiName(previous.getAwayTeam()))
                        .replaceAll("##awayTeamNameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(previous.getAwayTeam()))
                        .replaceAll("##score##", previous.getResult());
            }
            pageContents = pageContents.replace("##previousFixture##",
                    previousFixture);
            vkGateway.savePage(getGroupId(), "50872758", pageContents);
        } catch (Exception e) {
            throw new VkException("Failed to update next/previous fixture page.", e);
        }
    }
}

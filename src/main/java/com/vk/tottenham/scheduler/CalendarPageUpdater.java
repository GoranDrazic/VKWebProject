package com.vk.tottenham.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.footballapi.FootballApiGateway;
import com.vk.tottenham.footballapi.model.Competition;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.CompetitionSeason;
import com.vk.tottenham.footballapi.model.Fixture;
import com.vk.tottenham.footballapi.model.FixturesResponse;
import com.vk.tottenham.footballapi.model.Gameweek;
import com.vk.tottenham.footballapi.model.Kickoff;
import com.vk.tottenham.footballapi.model.Team;
import com.vk.tottenham.footballapi.model.TeamScore;
import com.vk.tottenham.mybatis.service.DictionaryService;

public class CalendarPageUpdater extends SchedulerBase {

    @Autowired
    @Qualifier("footballApiGateway")
    private FootballApiGateway footballApiGateway;

    @Autowired
    @Qualifier("dictionaryService")
    private DictionaryService dictionaryService;
    
    public static SimpleDateFormat monthFormatter = new SimpleDateFormat(
            "MMMMM", new Locale("ru"));

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "dd.MM.yy", Locale.US);

    private static final String MONTH_START_TEMPLATE = "{{Hider| <center>##month##</center>\n{|\n";
    private static final String MONTH_END_TEMPLATE = "|}\n}}\n";
    
    private static final String AWAY_FIXTURE_TEMPLATE = "|-\n|<center>##date##</center>||<center>[[##smallIcon##|35px;noborder;nolink|##competitionName##]]</center>||<center>«##homeTeam##» - '''«##awayTeam##»'''</center>||<center>##homeScore##:##awayScore##</center>\n";
    private static final String HOME_FIXTURE_TEMPLATE = "|-\n|<center>##date##</center>||<center>[[##smallIcon##|35px;noborder;nolink|##competitionName##]]</center>||<center>'''«##homeTeam##»''' - «##awayTeam##»</center>||<center>##homeScore##:##awayScore##</center>\n";

    @Override
    public void execute() {
        //Assume that this guy returns fixtures in chronological order. Not scheduled games go first. 
        //FixturesResponse fixtures = footballApiGateway.getAllFixtures();
        String month = "ДАТА НЕОПРЕДЕЛЕНА";
        Map<String, List<Fixture>> monthToFixture = new LinkedHashMap<>(); 
        FixturesResponse fixtures = getAllFixtures();
        for (Fixture fixture : fixtures.getContent()) {
            String fixtureMonth = "ДАТА НЕОПРЕДЕЛЕНА";
            Date date = fixture.getKickoff().getLabel();
            if (date != null) {
                fixtureMonth = monthFormatter.format(date).toUpperCase();
            }
            if (monthToFixture.get(fixtureMonth) == null) {
                monthToFixture.put(fixtureMonth, new LinkedList<>());
            }
            monthToFixture.get(fixtureMonth).add(fixture);
        }

        for (Entry<String, List<Fixture>> monthOfFixtures : monthToFixture.entrySet()) {
            //TODO: hide previous month
            //TODO: make scores unknown for future games
            //TODO: show postponed fixtures in the end
            //TODO: wrap everything in pageTemplate
            System.out.print(MONTH_START_TEMPLATE.replaceAll("##month##", monthOfFixtures.getKey()));
            for (Fixture fixture : monthOfFixtures.getValue()) {
                Date label = fixture.getKickoff().getLabel();
                String fixtureDate = label != null ? dateFormatter.format(label) : "Перенесён";
                String competitionName = fixture.getGameweek().getCompSeason().getCompetition().getDescription();
                String competitionNameInRussian = dictionaryService.translateInRussianNom(competitionName);
                String fixtureTemplate = "Tottenham Hotspur"
                        .equals(fixture.getTeams().get(0).getTeam().getName())
                                ? HOME_FIXTURE_TEMPLATE : AWAY_FIXTURE_TEMPLATE;
                fixtureTemplate = fixtureTemplate.replaceAll("##homeTeam##", fixture.getTeams().get(0).getTeam().getName())
                    .replaceAll("##awayTeam##", fixture.getTeams().get(1).getTeam().getName())
                    .replaceAll("##homeScore##", Integer.valueOf((int)fixture.getTeams().get(0).getScore()).toString())
                    .replaceAll("##awayScore##", Integer.valueOf((int)fixture.getTeams().get(1).getScore()).toString())
                    .replaceAll("##date##", fixtureDate)
                    .replaceAll("##smallIcon##", CompetitionName.getByName(competitionName).smallIcon())
                    .replaceAll("##competitionName##", competitionNameInRussian);
                System.out.print(fixtureTemplate);
            }
            System.out.print(MONTH_END_TEMPLATE);
        }
    }

    private FixturesResponse getAllFixtures() {
        FixturesResponse response = new FixturesResponse();
        List<Fixture> fixtures = new LinkedList<>();
        response.setContent(fixtures);

        fixtures.add(createFixture("Premier League", "Leicester City", 0.0, "Tottenham Hotspur", 0.0, null));
        fixtures.add(createFixture("Premier League", "Everton", 1.0, "Tottenham Hotspur", 1.0, "13.08.16"));
        fixtures.add(createFixture("Premier League", "Tottenham Hotspur", 1.0, "Crystal Palace", 0.0, "20.08.16"));
        fixtures.add(createFixture("Premier League", "Tottenham Hotspur", 1.0, "Livelpool", 1.0, "27.08.16"));
        fixtures.add(createFixture("Premier League", "Stoke City", 0.0, "Tottenham Hotspur", 4.0, "10.09.16"));
        fixtures.add(createFixture("UEFA Champions League", "Tottenham Hotspur", 1.0, "Monaco", 2.0, "14.09.16"));
        fixtures.add(createFixture("EFL Cup", "Tottenham Hotspur", 0.0, "Gillingham", 0.0, "21.09.16"));
        fixtures.add(createFixture("FA Cup", "Tottenham Hotspur", 2.0, "Aston Villa", 0.0, "08.01.16"));

        return response;
    }
    
    private Fixture createFixture(String competitionName, String homeTeamName, double homeTeamScore, String awayTeamName, double awayTeamScore, String dateString) {
        Fixture fixture = new Fixture();
        Gameweek gameweek = new Gameweek();
        CompetitionSeason compSeason = new CompetitionSeason();
        Competition competition = new Competition();
        competition.setDescription(competitionName);
        compSeason.setCompetition(competition);
        gameweek.setCompSeason(compSeason );
        fixture.setGameweek(gameweek);
        
        List<TeamScore> teamScores = new LinkedList<>();
        TeamScore teamScore = new TeamScore();
        teamScore.setScore(homeTeamScore);
        Team team = new Team();
        team.setName(homeTeamName);
        teamScore.setTeam(team);
        teamScores.add(teamScore);
        fixture.setTeams(teamScores);
        
        teamScore = new TeamScore();
        teamScore.setScore(awayTeamScore);
        team = new Team();
        team.setName(awayTeamName);
        teamScore.setTeam(team);
        teamScores.add(teamScore);

        Kickoff kickoff = new Kickoff();
        try {
            kickoff.setLabel(
                    dateString != null ? dateFormatter.parse(dateString) : null);
        } catch (ParseException e) {
        }
        fixture.setKickoff(kickoff);
        
        return fixture;
    }

    public static void main(String[] args) {
        new CalendarPageUpdater().execute();
    }
}

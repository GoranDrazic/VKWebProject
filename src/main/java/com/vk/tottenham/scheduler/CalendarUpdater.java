package com.vk.tottenham.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.footballapi.FootballApiGateway;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.CompetitionsResponse;
import com.vk.tottenham.footballapi.model.Fixture;
import com.vk.tottenham.footballapi.model.FixturesResponse;
import com.vk.tottenham.mybatis.service.FixtureService;
import com.vk.tottenham.utils.CompSeasonUtil;

public class CalendarUpdater extends SchedulerBase {

    private FixtureService fixtureService;

    @Autowired
    @Qualifier("footballApiGateway")
    private FootballApiGateway footballApiGateway;  

    @Override
    public void execute() {
        CompetitionsResponse competitionsResponse = footballApiGateway.getCompetitions();
        for (CompetitionName competition : CompetitionName.values()) {
            String compSeason = CompSeasonUtil.getCompSeason(competitionsResponse, competition);
            FixturesResponse fr = footballApiGateway.getFixtures(competition.comp(), compSeason);
            List<Fixture> fixtures = fr.getContent();
            for (Fixture newFixture : fixtures) {
                com.vk.tottenham.core.model.Fixture fixture = new com.vk.tottenham.core.model.Fixture();
                fixture.setId((int)newFixture.getId());
                fixture.setAwayTeam(newFixture.getTeams().get(1).getTeam().getName());
                fixture.setCompetition(newFixture.getGameweek().getCompSeason().getCompetition().getDescription());
                fixture.setDate(newFixture.getKickoff().getLabel());
                fixture.setHomeTeam(newFixture.getTeams().get(0).getTeam().getName());
                fixture.setResult((int)newFixture.getTeams().get(0).getScore() + ":"
                        + (int)newFixture.getTeams().get(1).getScore());
                fixture.setSeason(newFixture.getGameweek().getCompSeason().getLabel());
                fixture.setStadium(newFixture.getGround().getName());
                fixture.setCity(newFixture.getGround().getCity());
                fixture.setGameweek((int)newFixture.getGameweek().getGameweek());

                com.vk.tottenham.core.model.Fixture existingFixture = fixtureService.find((int)fixture.getId());
                if (existingFixture == null) {
                    fixtureService.save(fixture);
                } else {
                    fixtureService.update(fixture);
                }
            }
        }
    }

    public void setFixtureService(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }
}

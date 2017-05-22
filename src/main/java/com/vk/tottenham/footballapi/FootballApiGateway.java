package com.vk.tottenham.footballapi;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.CompetitionsResponse;
import com.vk.tottenham.footballapi.model.Fixture;
import com.vk.tottenham.footballapi.model.FixturesResponse;
import com.vk.tottenham.footballapi.model.SquadResponse;
import com.vk.tottenham.footballapi.model.StandingsResponse;
import com.vk.tottenham.footballapi.model.StatsResponse;
import com.vk.tottenham.utils.CompSeasonUtil;

@Component("footballApiGateway")
public class FootballApiGateway {

    @Value("${com.vk.tottenham.footballapi.url}")
    private String footballApiUrl;

    @Value("${com.vk.tottenham.footballapi.assistsPath}")
    private String assistsPath;

    @Value("${com.vk.tottenham.footballapi.goalsPath}")
    private String goalsPath;

    @Value("${com.vk.tottenham.footballapi.fixturesPath}")
    private String fixturesPath;

    @Value("${com.vk.tottenham.footballapi.squadPath}")
    private String squadPath;

    @Value("${com.vk.tottenham.footballapi.standingsPath}")
    private String standingsPath;

    @Value("${com.vk.tottenham.footballapi.competitionsPath}")
    private String competitionsPath;
    
    @Value("${com.vk.tottenham.footballapi.fixturePath}")
    private String fixturePath;

    @Autowired
    @Qualifier("footballApiRestTemplate")
    private RestOperations footballApiRestTemplate;

    public StatsResponse getAssistStats(String comp, String compSeason) {
        return getStats(assistsPath, comp, compSeason);
    }

    public StatsResponse getGoalStats(String comp, String compSeason) {
        return getStats(goalsPath, comp, compSeason);
    }

    public FixturesResponse getFixtures(String comp, String compSeason) {
        return getFixtures(fixturesPath, comp, compSeason);
    }

    public SquadResponse getSquad(String compSeason) {
        return getSquad(squadPath, compSeason);
    }

    public StandingsResponse getStandings(String comp, String compSeason) {
        return getStandings(standingsPath, comp, compSeason);
    }

    public CompetitionsResponse getCompetitions() {
        return getCompetitions(competitionsPath);
    }

    public Fixture getFixture(int id) {
        return getFixture(fixturePath, id); 
    }

    public FixturesResponse getAllFixtures() {
        CompetitionsResponse competitionsResponse = getCompetitions();
        StringBuilder compSeasonBuilder = new StringBuilder(); 
        for (CompetitionName competition : CompetitionName.values()) {
            String compSeason = CompSeasonUtil.getCompSeason(competitionsResponse, competition);
            compSeasonBuilder.append(compSeasonBuilder.length() == 0 ? "" : ",")
                    .append(compSeason);
        }
        return getFixtures(fixturesPath, null, compSeasonBuilder.toString());
    }

    private CompetitionsResponse getCompetitions(String path) {
        //page=0&pageSize=100&detail=2
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("pageSize", "100");
        params.add("detail", "2");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                footballApiUrl + path)
                .queryParams(params);

        return callApi(builder.build().encode().toUri(), CompetitionsResponse.class);
    }

    private StandingsResponse getStandings(String path, String comp, String compSeason) {
        //compSeasons=54&altIds=true&detail=2&FOOTBALL_COMPETITION=1
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("compSeasons", compSeason);
        params.add("FOOTBALL_COMPETITION", comp);
        params.add("detail", "2");
        params.add("altIds", "true");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                footballApiUrl + path)
                .queryParams(params);

        return callApi(builder.build().encode().toUri(), StandingsResponse.class);
    }

    private SquadResponse getSquad(String path, String compSeason) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("pageSize", "50");
        params.add("compSeasons", compSeason);
        params.add("type", "player");
        params.add("teams", "21");
        params.add("altIds", "true");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                footballApiUrl + path.replace("{{compseason}}", compSeason))
                .queryParams(params);

        return callApi(builder.build().encode().toUri(), SquadResponse.class);
    }

    private StatsResponse getStats(String path, String comp, String compSeason) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("pageSize", "50");
        params.add("compSeasons", compSeason);
        params.add("comps", comp);
        params.add("compCodeForActivePlayer", "EN_PR");
        params.add("teams", "21");
        params.add("altIds", "true");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(footballApiUrl + path).queryParams(params);

        return callApi(builder.build().encode().toUri(), StatsResponse.class);
    }

    private FixturesResponse getFixtures(String path, String comp, String compSeason) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("pageSize", "70");
        params.add("compSeasons", compSeason);
        if (comp != null) {
            params.add("comps", comp);
        }
        params.add("teams", "21");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(footballApiUrl + path).queryParams(params);

        return callApi(builder.build().encode().toUri(), FixturesResponse.class);
    }

    private Fixture getFixture(String path, int id) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(footballApiUrl + path.replace("{{fixture}}", String.valueOf(id)));

        return callApi(builder.build().encode().toUri(), Fixture.class);
    }

    private <T> T callApi (URI uri, Class<T> reponseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin("https://www.premierleague.com");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                null, headers);
        ResponseEntity<T> response = footballApiRestTemplate.exchange(
                uri, HttpMethod.GET, request,
                reponseClass);
        return response.getBody();
    }
}

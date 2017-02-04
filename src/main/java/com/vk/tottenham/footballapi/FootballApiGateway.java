package com.vk.tottenham.footballapi;

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
import com.vk.tottenham.footballapi.model.FixturesResponse;
import com.vk.tottenham.footballapi.model.StatsResponse;

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

    @Autowired
    @Qualifier("footballApiRestTemplate")
    private RestOperations footballApiRestTemplate;

    public StatsResponse getAssistStats(CompetitionName competition) {
        return getStats(assistsPath, competition);
    }

    public StatsResponse getGoalStats(CompetitionName competition) {
        return getStats(goalsPath, competition);
    }

    public FixturesResponse getFixtures(CompetitionName competition) {
        return getFixtures(fixturesPath, competition);
    }

    private StatsResponse getStats(String path, CompetitionName competition) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("pageSize", "50");
        params.add("compSeasons", competition.compSeason());
        params.add("comps", competition.comp());
        params.add("compCodeForActivePlayer", "EN_PR");
        params.add("teams", "21");
        params.add("altIds", "true");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(footballApiUrl + path).queryParams(params);

        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin("https://www.premierleague.com");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                null, headers);
        ResponseEntity<StatsResponse> response = footballApiRestTemplate.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, request,
                StatsResponse.class);
        return response.getBody();
    }

    private FixturesResponse getFixtures(String path, CompetitionName competition) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("pageSize", "50");
        params.add("compSeasons", competition.compSeason());
        params.add("comps", competition.comp());
        params.add("teams", "21");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(footballApiUrl + path).queryParams(params);

        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin("https://www.premierleague.com");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                null, headers);
        ResponseEntity<FixturesResponse> response = footballApiRestTemplate.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, request,
                FixturesResponse.class);
        return response.getBody();
    }
}

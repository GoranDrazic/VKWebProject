package com.vk.tottenham.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vk.tottenham.core.model.Fixture;
import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.model.Slide;
import com.vk.tottenham.model.Summary;
import com.vk.tottenham.mybatis.service.FixtureService;
import com.vk.tottenham.mybatis.service.PlayerService;
import com.vk.tottenham.mybatis.service.TeamService;
import com.vk.tottenham.utils.PhotoDownloader;

public class MatchInfoLoader extends SchedulerBase {

    private static final long TIME_BEFORE_LOADING_PHOTOS = -1000 * 60 * 10;

    private static final long TIME_AFTER_LOADING_PHOTOS = 1000 * 60 * 60 * 24 * 7;

    private static final String ZIMBIO_PLAYER_PHOTOS_URL = "http://www.zimbio.com/api/v1/cached/photostream?filters[]={{name}}+{{surname}}&type=solo&count=16&offset=-6";

    private static final String ALBUM_TEMPLATE = "http://www.zimbio.com/api/v1/cached/pictures?filters[]={{album_id}}&count=500&offset={{offset}}";
    
    private static final String ALBUM_DATE_FORMAT = "dd.MM.yy";

    private static SimpleDateFormat formatter = new SimpleDateFormat(
            ALBUM_DATE_FORMAT);
    static {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private FixtureService fixtureService;
    private PlayerService playerService;
    private TeamService teamService;
    @Autowired
    private PhotoDownloader photoDownloader;
    
    private static HttpClient client = HttpClientBuilder.create().build();

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public void execute() {
        try {
            Fixture nearestFixture = fixtureService.getNearestFixture();
            if (nearestFixture == null)
                return;
            long timeBeforeGame = System.currentTimeMillis()
                    - nearestFixture.getDate().getTime();

            if (timeBeforeGame > TIME_BEFORE_LOADING_PHOTOS
                    && timeBeforeGame < TIME_AFTER_LOADING_PHOTOS) {
                loadPhotos(nearestFixture);
            }

        } catch (Exception e) {
            throw new VkException("Failed to load photos.", e);
        }
    }

    private void loadPhotos(Fixture fixture) throws ClientProtocolException, IOException {
        // String albumId = fixtureService.getAlbumId(nearestFixture);
        if (fixture.getAlbumId() == null) {
            List<Player> players = playerService.findAll();
            for (Player player: players) {
                if (player.getSquadNumber() == 0) continue;
                String url = ZIMBIO_PLAYER_PHOTOS_URL
                        .replace("{{name}}", player.getName())
                        .replace("{{surname}}", player.getSurname());
                Summary summary = getSummary(url);
                List<Slide> slides = summary.getSlides();
                if (slides.isEmpty()) continue;
                String title = summary.getSlides().get(0).getTitle();
                String zimbioHomeName = teamService.getZimbioNameByFapiName(fixture.getHomeTeam());
                String zimbioAwayName = teamService.getZimbioNameByFapiName(fixture.getAwayTeam());
                if(title.contains(zimbioHomeName) && title.contains(zimbioAwayName)) {
                    String zimbioAlbumId = getAlbumId(summary.getSlides().get(0).getRegionCaption());
                    fixture.setZimbioAlbumId(zimbioAlbumId);
                    fixtureService.setZimbioAlbumId(fixture);
                    String albumId = vkGateway.createAlbum(getGroupId(),
                            formatter.format(fixture.getDate()) + " | «"
                                    + teamService
                                            .getRussianTeamNameInNominativeByFapiName(
                                                    fixture.getHomeTeam())
                                    + "» - «"
                                    + teamService
                                            .getRussianTeamNameInNominativeByFapiName(
                                                    fixture.getAwayTeam())
                                    + "»", "");
                    fixture.setAlbumId(albumId);
                    fixtureService.setAlbumId(fixture);
                    break;
                }
            }
        }
        if (fixture.getAlbumId() != null) {
            int albumSize = vkGateway.getAlbumSize(getGroupId(), fixture.getAlbumId());
            String albumUrl = ALBUM_TEMPLATE.replace("{{album_id}}", fixture.getZimbioAlbumId()).replace("{{offset}}", String.valueOf(albumSize - 1));
            Summary summary = getSummary(albumUrl);
            List<Slide> slides = summary.getSlides();
            for (Slide slide : slides) {
                String largePhoto = slide.getImgUrl().replace("l.jpg", "x.jpg");
                photoDownloader.downloadPhoto(largePhoto, fixture.getAlbumId(), isTestMode, true);
            }
        }
    }

    public void setFixtureService(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    private static Summary getSummary(String url)
            throws ClientProtocolException, IOException {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        response.getEntity().getContent().close();
        return mapper.readValue(result.toString(), Summary.class);

    }

    private String getAlbumId(String regionalCaption) {
        Pattern pattern = Pattern.compile("href=\\\"(.*?)\\\">more");
        Matcher matcher = pattern.matcher(regionalCaption);
        String s = null;
        while (matcher.find()) {
            s = matcher.group(1);
        }
        return s.split("/")[2];
    }
}

package com.vk.tottenham.scheduler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.model.FantasyResult;
import com.vk.tottenham.model.FantasySummary;
import com.vk.tottenham.model.GameWeek;
import com.vk.tottenham.model.GameWeekSummary;

public class FantasyResultsPoster extends SchedulerBase {

    private final static String LEAGUE_STANDINGS_URL = "https://fantasy.premierleague.com/drf/leagues-classic-standings/62711";
    
    private final static String GAME_WEEK_INFO_URL = "https://fantasy.premierleague.com/drf/bootstrap-static";

    private static HttpClient client = HttpClientBuilder.create().build();

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public void execute() {
        try {
            int week = 18;
            GameWeek gameWeek = getLastGameWeek(week);
            if (gameWeek.isDataChecked()) {
                FantasySummary fantasySummary = getFantasySummary(LEAGUE_STANDINGS_URL);
                int maxScore = 0;
                List<String> maxScorers = new LinkedList<>();
                List<String> mostImproved = new LinkedList<>();
                int biggestImprovement = 0;
                List<FantasyResult> results = fantasySummary.getStandings().getResults();
                for (FantasyResult fantasyResult : results) {
                    if (fantasyResult.getEventTotal() > maxScore) {
                        maxScore = fantasyResult.getEventTotal();
                        maxScorers.clear();
                        maxScorers.add(fantasyResult.getPlayerName());
                    } else if (fantasyResult.getEventTotal() == maxScore) {
                        maxScorers.add(fantasyResult.getPlayerName());
                    }
                    int improvement = fantasyResult.getLastRank() - fantasyResult.getRank();
                    if (improvement > biggestImprovement) {
                        biggestImprovement = improvement;
                        mostImproved.clear();
                        mostImproved.add(fantasyResult.getPlayerName());
                    } else if (improvement == biggestImprovement) {
                        mostImproved.add(fantasyResult.getPlayerName());
                    }
                }
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append("Результаты ").append(week)
                        .append("-го тура нашего фэнтэзи турнира.\n\n");
                messageBuilder.append("Лидирующая тройка после текущего тура выглядит следущим образом:\n");
                for(int i=0; i<3; i++) {
                    messageBuilder.append(i+1).append(". ")
                            .append(results.get(i).getPlayerName()).append("(")
                            .append(results.get(i).getLastRank()).append(")\n");
                }
                messageBuilder.append("\nНаибольшее количество очков(")
                        .append(maxScore).append(") получил").append(maxScorers.size() == 1 ? " " : "и ")
                        .append(String.join(", ", maxScorers)).append(".\n\n");
                if (mostImproved.size() <= 5) {
                    messageBuilder.append("На наибольшее количество позиций(")
                            .append(biggestImprovement)
                            .append(") смог").append(mostImproved.size() == 1 ? "" : "ли").append(" улучшить свое подожение: ")
                            .append(String.join(", ", mostImproved)).append(".\n\n");
                }
                messageBuilder.append("Не забывайте следить за эффективностью своей команды!");

                //456243703
                vkGateway.postOnWall(getGroupId(), getGroupId(), messageBuilder.toString(), "456240248", -1);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private GameWeekSummary getGameWeekSummary(String url) throws ClientProtocolException, IOException {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        GameWeekSummary t = mapper.readValue(response.getEntity().getContent(),
                new TypeReference<GameWeekSummary>() {
                });
        response.getEntity().getContent().close();
        return t;
    }
    
    private FantasySummary getFantasySummary(String url) throws ClientProtocolException, IOException {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        FantasySummary t = mapper.readValue(response.getEntity().getContent(),
                new TypeReference<FantasySummary>() {
                });
        response.getEntity().getContent().close();
        return t;
    }

    private GameWeek getLastGameWeek(int week) throws ClientProtocolException, IOException {
        GameWeekSummary gameWeekSummary = getGameWeekSummary(GAME_WEEK_INFO_URL);
        for (GameWeek gameWeek : gameWeekSummary.getEvents()) {
            if (gameWeek.getId() == week) {
                return gameWeek;
            }
        }
        throw new VkException("No game week found.");
    }
}

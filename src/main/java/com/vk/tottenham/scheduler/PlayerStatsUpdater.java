package com.vk.tottenham.scheduler;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.footballapi.FootballApiGateway;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.Name;
import com.vk.tottenham.footballapi.model.PlayerStatistics;
import com.vk.tottenham.footballapi.model.StatsResponse;
import com.vk.tottenham.mybatis.service.PlayerService;

public class PlayerStatsUpdater extends SchedulerBase {

    private static final String STATS_ROW = "|-\n| <center>##squadNumber##</center> || <center>"
            + "[[##photoId##|150px;nolink|##nameInRussian##]]</center> || "
            + "##nameInRussian## || <center>##goals##</center> || <center>##assists##</center>\n";

    @Autowired
    @Qualifier("footballApiGateway")
    private FootballApiGateway footballApiGateway;

    @Autowired
    @Qualifier("playerService")
    private PlayerService playerService;

    @Override
    public void execute() {
        for (CompetitionName competition : CompetitionName.values()) {
            if (competition == CompetitionName.UEFA_CHAMPIONS_LEAGUE 
                    || competition == CompetitionName.UEFA_EUROPA_LEAGUE) return;
            try {
                StatsResponse assistsResponse = footballApiGateway.getAssistStats(competition);
                StatsResponse goalResponse = footballApiGateway.getGoalStats(competition);
                Map<Name, GoalAssist> playerMap = new LinkedHashMap<>();
                for (PlayerStatistics playerStatistics : goalResponse.getStats()
                        .getContent()) {
                    Name name = playerStatistics.getOwner().getName();
                    int goals = (int) playerStatistics.getValue();
                    playerMap.put(name, new GoalAssist(goals, 0));

                }
                for (PlayerStatistics playerStatistics : assistsResponse.getStats()
                        .getContent()) {
                    Name name = playerStatistics.getOwner().getName();
                    int assists = (int) playerStatistics.getValue();
                    if (playerMap.get(name) != null) {
                        playerMap.get(name).setAssists(assists);
                    } else {
                        playerMap.put(name, new GoalAssist(0, assists));
                    }
                }
                StringBuilder stats = new StringBuilder();
                for (Entry<Name, GoalAssist> entry : playerMap.entrySet()) {
                    String[] nameSurname = entry.getKey().getDisplay().split(" ");
                    Player searchPlayer = new Player(nameSurname[0], nameSurname[1]);
                    Player player = playerService.findNameAndSurname(searchPlayer);
                    String row = new String(STATS_ROW);
                    row = row
                            .replaceAll("##goals##",
                                    String.valueOf(entry.getValue().getGoals()))
                            .replaceAll("##assists##",
                                    String.valueOf(entry.getValue().getAssists()))
                            .replaceAll("##squadNumber##",
                                    String.valueOf(player.getSquadNumber()))
                            .replaceAll("##photoId##", player.getStatsPhoto())
                            .replaceAll("##nameInRussian##", player.getRussianNameNom());
                    stats.append(row);
                }
                URL url = Resources
                        .getResource(competition.statsTemplate());
                String pageContents = Resources.toString(url, Charsets.UTF_8);
                pageContents = pageContents.replace("##statsTable##", stats);
                vkGateway.savePage(getGroupId(), competition.statsPage(), pageContents);
            } catch (Exception e) {
                throw new VkException("Exception updating standing.", e);
            }
        }
    }

    private static class GoalAssist {
        public GoalAssist(int goals, int assists) {
            super();
            this.goals = goals;
            this.assists = assists;
        }

        private int goals;
        private int assists;

        public int getGoals() {
            return goals;
        }

        public int getAssists() {
            return assists;
        }

        public void setAssists(int assists) {
            this.assists = assists;
        }
    }
}

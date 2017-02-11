package com.vk.tottenham.template;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.footballapi.model.Name;
import com.vk.tottenham.footballapi.model.PlayerStatistics;
import com.vk.tottenham.mybatis.service.PlayerService;

@Component("statsTemplate")
public class StatsTemplate extends TemplateBase {

    private static final String TABLE_HEADER = "{|\n|-\n! <center><gray>Статистика игроков</gray></center>\n|}\n{|\n|-\n! <center><gray>#</gray></center> !! <center><gray>Фото</gray></center> !! <center><gray>Игрок</gray></center> !! <center><gray>Голы</gray></center> !! <center><gray>Ассисты</gray></center>\n";
    private static final String TABLE_FOOTER = "|}\n\n";

    private static final String STATS_ROW = "|-\n| <center>##squadNumber##</center> || <center>"
            + "[[##photoId##|150px;nolink|##nameInRussian##]]</center> || "
            + "##nameInRussian## || <center>##goals##</center> || <center>##assists##</center>\n";

    @Autowired
    @Qualifier("playerService")
    private PlayerService playerService;

    @Override
    public boolean isUpdateNeeded(PageContext context) {
        return context.getAssists().getStats().getContent().size() > 0
                || context.getGoals().getStats().getContent().size() > 0;
    }

    @Override
    public String buildContent(PageContext context) {
        Map<Name, GoalAssist> playerMap = new LinkedHashMap<>();
        for (PlayerStatistics playerStatistics : context.getGoals().getStats()
                .getContent()) {
            Name name = playerStatistics.getOwner().getName();
            int goals = (int) playerStatistics.getValue();
            playerMap.put(name, new GoalAssist(goals, 0));

        }
        for (PlayerStatistics playerStatistics : context.getAssists().getStats()
                .getContent()) {
            Name name = playerStatistics.getOwner().getName();
            int assists = (int) playerStatistics.getValue();
            if (playerMap.get(name) != null) {
                playerMap.get(name).setAssists(assists);
            } else {
                playerMap.put(name, new GoalAssist(0, assists));
            }
        }
        StringBuilder stats = new StringBuilder(TABLE_HEADER);
        for (Entry<Name, GoalAssist> entry : playerMap.entrySet()) {
            Player searchPlayer = new Player(entry.getKey().getFirst(), entry.getKey().getLast());
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
        return stats.append(TABLE_FOOTER).toString();
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

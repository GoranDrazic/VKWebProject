package com.vk.tottenham.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vk.tottenham.footballapi.model.Fixture;
import com.vk.tottenham.footballapi.model.Table;
import com.vk.tottenham.footballapi.model.TableEntry;
import com.vk.tottenham.mybatis.service.TeamService;

@Component("groupStageTemplate")
public class GroupStageTemplate extends TemplateBase {

    private static final String TABLE_HEADER = "{|\n|- \n! <center><gray>М</gray></center> !! <center><gray>Команда</gray></center> !! <center><gray>И</gray></center> !! <center><gray>В</gray></center> !! <center><gray>Н</gray></center> !! <center><gray>П</gray></center> !! <center><gray>М</gray></center> !! <center><gray>О</gray></center>\n";

    private static final String STANDINGS_ROW = "|-\n| <center>##position##</center> || "
            + "[[##photoId##|20px;noborder;nolink|«##nameInRussian##»]] "
            + "«##nameInRussian##» || <center>##played##</center> || "
            + "<center>##won##</center> || <center>##drawed##</center> || "
            + "<center>##lost##</center> || <center>##goalsScored##-##goalsConceeded##</center> || "
            + "<center>##points##</center>\n";
    
    private static final String TTH_STANDINGS_ROW = "|-\n! <center>##position##</center> !! "
            + "[[##photoId##|20px;noborder;nolink|«##nameInRussian##»]] "
            + "«##nameInRussian##»  !! <center>##played##</center> !! "
            + "<center>##won##</center> !! <center>##drawed##</center> !! "
            + "<center>##lost##</center> !! <center>##goalsScored##-##goalsConceeded##</center> !! "
            + "<center>##points##</center>\n";
    
    private static final String MATCH_ROW = "|-\n| ##tour##-й тур\n| <b>«##homeTeam##»</b> - «##awayTeam##»\n| <b>##homeGoals##:##awayGoals##</b>\n";

    @Autowired
    @Qualifier("teamService")
    private TeamService teamService;

    @Override
    public boolean isUpdateNeeded(PageContext context) {
        List<Table> tables = context.getStandings().getTables();
        for (Table table : tables) {
            for (TableEntry tableEntry : table.getEntries()) {
                if ("Tottenham Hotspur".equals(tableEntry.getTeam().getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String buildContent(PageContext context) {
        List<Table> tables = context.getStandings().getTables();
        Table groupTable = null;
        for (Table table : tables) {
            for (TableEntry tableEntry : table.getEntries()) {
                if ("Tottenham Hotspur".equals(tableEntry.getTeam().getName())) {
                    groupTable = table;
                }
            }
        }
        StringBuilder builder = new StringBuilder(TABLE_HEADER);
        for (TableEntry tableEntry : groupTable.getEntries()) {
            String row = !tableEntry.getTeam().getName().equals("Tottenham Hotspur")
                    ? new String(STANDINGS_ROW) : new String(TTH_STANDINGS_ROW);
            row = row.replaceAll("##position##", String.valueOf(tableEntry.getPosition()))
                    .replaceAll("##photoId##", teamService.getStandingsIconByFapiName(tableEntry.getTeam().getName()))
                    .replaceAll("##nameInRussian##", teamService.getRussianTeamNameInNominativeByFapiName(tableEntry.getTeam().getName()))
                    .replaceAll("##played##", String.valueOf(tableEntry.getOverall().getPlayed()))
                    .replaceAll("##won##", String.valueOf(tableEntry.getOverall().getWon()))
                    .replaceAll("##drawed##", String.valueOf(tableEntry.getOverall().getDrawn()))
                    .replaceAll("##lost##", String.valueOf(tableEntry.getOverall().getLost()))
                    .replaceAll("##goalsScored##", String.valueOf(tableEntry.getOverall().getGoalsFor()))
                    .replaceAll("##goalsConceeded##", String.valueOf(tableEntry.getOverall().getGoalsAgainst()))
                    .replaceAll("##points##", String.valueOf(tableEntry.getOverall().getPoints()));
            builder.append(row);
        }
        builder.append("|}\n\n{|\n");
        List<Fixture> fixtures = context.getFixtures().getContent();
        for (int i=0; i<6 && i<fixtures.size(); i++) {
            Fixture fixture = fixtures.get(i);
            String row = new String(MATCH_ROW);
            row = row.replace("##homeTeam##", teamService.getRussianTeamNameInNominativeByFapiName(fixture.getTeams().get(0).getTeam().getName()))
            .replace("##awayTeam##", teamService.getRussianTeamNameInNominativeByFapiName(fixture.getTeams().get(1).getTeam().getName()))
            .replace("##homeGoals##", String.valueOf((int)fixture.getTeams().get(0).getScore()))
            .replace("##awayGoals##", String.valueOf((int)fixture.getTeams().get(1).getScore()))
            .replace("##tour##", String.valueOf(i+1));
            builder.append(row);
        }
        return builder.append("|}").toString();
    }

}

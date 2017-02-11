package com.vk.tottenham.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vk.tottenham.footballapi.model.TableEntry;
import com.vk.tottenham.mybatis.service.TeamService;

@Component("tableTemplate")
public class TableTemplate extends TemplateBase {

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

    @Autowired
    @Qualifier("teamService")
    private TeamService teamService;

    @Override
    public boolean isUpdateNeeded(PageContext context) {
        return context.getStandings().getTables() != null && 
                context.getStandings().getTables().size() > 0 && 
                context.getStandings().getTables().get(0).getEntries() != null;
    }

    @Override
    public String buildContent(PageContext context) {
        StringBuilder builder = new StringBuilder(TABLE_HEADER);
        for (TableEntry tableEntry : context.getStandings().getTables().get(0).getEntries()) {
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
        return builder.append("|}\n\n").toString();
    }

}

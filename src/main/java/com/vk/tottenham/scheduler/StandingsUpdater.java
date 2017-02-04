package com.vk.tottenham.scheduler;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.TeamService;
import com.google.common.base.Charsets;
import  com.google.common.io.Resources;

public class StandingsUpdater extends SchedulerBase {

    private static final String FIXTURES_URL = "http://www.tottenhamhotspur.com/matches/league-table/";
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
    private TeamService teamService;

    @Override
    public void execute() {
        try {
            Document content = Jsoup.parse(new URL(FIXTURES_URL), 10000);
            Element tableDiv = content.getElementsByClass("leaguetable").first();
            Elements trs = tableDiv.getElementsByTag("table").first().getElementsByTag("tbody").first().getElementsByTag("tr");
            StringBuilder standings = new StringBuilder();
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                String row;
                if ("Tottenham Hotspur".equals(tds.get(1).text())) {
                    row = new String(TTH_STANDINGS_ROW); 
                } else {
                    row = new String(STANDINGS_ROW);
                }
                row = row.replaceAll("##position##", tds.get(0).text())
                        .replaceAll("##photoId##", teamService.getStandingsIconByZimbioName(tds.get(1).text()))
                        .replaceAll("##nameInRussian##", teamService.getRussianTeamNameInNominativeByZimbioName(tds.get(1).text()))
                        .replaceAll("##played##", tds.get(2).text())
                        .replaceAll("##won##", tds.get(3).text())
                        .replaceAll("##drawed##", tds.get(4).text())
                        .replaceAll("##lost##", tds.get(5).text())
                        .replaceAll("##goalsScored##", tds.get(6).text())
                        .replaceAll("##goalsConceeded##", tds.get(7).text())
                        .replaceAll("##points##", tds.get(9).text());
                standings.append(row);
            }
            URL url = Resources.getResource("com/vk/tottenham/contents/standingsContents.txt");
            String pageContents = Resources.toString(url, Charsets.UTF_8);
            pageContents = pageContents.replace("##standingsTable##", standings);
            vkGateway.savePage(getGroupId(), "36552750", pageContents);
        } catch (Exception e) {
            throw new VkException("Exception updating standing.", e);
        }
    }
}

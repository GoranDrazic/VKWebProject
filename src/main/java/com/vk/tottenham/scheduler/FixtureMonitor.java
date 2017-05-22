package com.vk.tottenham.scheduler;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.core.model.Fixture;
import com.vk.tottenham.footballapi.FootballApiGateway;
import com.vk.tottenham.footballapi.model.Clock;
import com.vk.tottenham.footballapi.model.Event;
import com.vk.tottenham.footballapi.model.LineupPlayer;
import com.vk.tottenham.mybatis.service.DictionaryService;
import com.vk.tottenham.mybatis.service.FixtureService;
import com.vk.tottenham.utils.PhotoDownloader;

public class FixtureMonitor extends SchedulerBase {

    @Autowired
    @Qualifier("fixtureService")
    private FixtureService fixtureService;

    @Autowired
    @Qualifier("dictionaryService")
    private DictionaryService dictionaryService;

    @Autowired
    @Qualifier("footballApiGateway")
    private FootballApiGateway footballApiGateway;

    @Autowired
    @Qualifier("photoDownloader")
    private PhotoDownloader photoDownloader;

    Pattern numberPattern = Pattern.compile("\\d+");

    @Override
    public void execute() {
        Fixture currentFixture = fixtureService.getCurrentFixture();
        if (currentFixture == null) return;
        com.vk.tottenham.footballapi.model.Fixture updatedFixture = footballApiGateway
                .getFixture(currentFixture.getId());
        createPicture(updatedFixture.getScore(), getTime(updatedFixture.getClock()),
                getGoalScorers(true, updatedFixture),
                getGoalScorers(false, updatedFixture));
    }

    private void createPicture(String score, String time, List<String> homeGoalScorers, List<String> awayGoalScorers) {
        try {
            BufferedImage image = ImageIO.read(new File(
                    "src/main/resources/images/template.png"));
            
            Graphics g = image.getGraphics();
            Font semibold = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/images/Panton Semibold.ttf"));
            g.setFont(semibold.deriveFont(60f));
            g.drawString("LIVE", 735, 110);//x, y
            
            Font regular = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/images/Panton.ttf"));
            g.setFont(regular.deriveFont(160f));
            g.drawString(score, 690, 275);//x, y
            
            g.setFont(semibold.deriveFont(36f));
            g.drawString(time, 770, 350);//x, y

            g.setFont(semibold.deriveFont(18f));
            for (int i=0; i< homeGoalScorers.size(); i++) {
                g.drawString(homeGoalScorers.get(i).toUpperCase(), 520, 200 + i*25);//x, y
            }

            for (int i=0; i< awayGoalScorers.size(); i++) {
                g.drawString(awayGoalScorers.get(i).toUpperCase(), 970, 200 + i*25);//x, y
            }

            g.dispose();
        
            ImageIO.write(image, "png", new File("src/main/resources/images/Result.png"));

            photoDownloader.uploadCoverPhoto("15474997", "src/main/resources/images/Result.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FontFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    private String getTime(Clock clock) {
        String minute = "0";
        if (clock != null) {
            Matcher numberMatcher = numberPattern.matcher(clock.getLabel());
            if (numberMatcher.find()) {
                minute = numberMatcher.group();
            }
        }
        //90 +6'00
        //45 +3'00
        //45'00
        return minute + '\'';
    }

    private List<String> getGoalScorers(boolean homeTeam, com.vk.tottenham.footballapi.model.Fixture fixture) {
        List<String> goalScorers = new ArrayList<>();
        Map<Integer, String> id2Name = new HashMap<>();
        for (LineupPlayer player : fixture.getTeamLists().get(homeTeam ? 0 : 1).getLineup()) {
            id2Name.put(player.getId(), player.getName().getDisplay());
        }
        
        for (LineupPlayer player : fixture.getTeamLists().get(homeTeam ? 0 : 1).getSubstitutes()) {
            id2Name.put(player.getId(), player.getName().getDisplay());
        }

        int teamId = (int) fixture.getTeams().get(homeTeam ? 0 : 1).getTeam().getId();
        for (Event event : fixture.getEvents()) {
            if ("G".equals(event.getType()) && teamId == event.getTeamId()) {
                goalScorers.add(dictionaryService.translateInRussianNom(id2Name.get(event.getPersonId())) + " " + getTime(event.getClock()));
            }
        }
        return goalScorers;
    }
}

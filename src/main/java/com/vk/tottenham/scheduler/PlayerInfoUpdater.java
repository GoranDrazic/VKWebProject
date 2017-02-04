package com.vk.tottenham.scheduler;

import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.core.model.Position;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.mybatis.service.PlayerService;

public class PlayerInfoUpdater extends SchedulerBase {
    private static final String NAME_SURNAME = "Name";
    private static final String DATE_OF_BIRTH = "Born";
    private static final String POSITION = "Position";
    private static final String JOINED_SPURS = "Joined Spurs";
    private static final String PREVIOUS_CLUBS = "Previous Club(s)";
    private static final String SQUAD_NUMBER = "Squad number";

    public static SimpleDateFormat formatter = new SimpleDateFormat(
            "dd MMMM yyyy", Locale.US);

    static {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private static final Logger LOGGER = Logger.getLogger(PlayerInfoUpdater.class);
    private static String[] playerLinks = new String[] {
            "http://www.tottenhamhotspur.com/first-team-profiles/",
            "http://www.tottenhamhotspur.com/development-squad/" };

    private PlayerService playerService;

    @Override
    public void execute() {
        List<Player> playerList = playerService.findAll();
        Map<String, Player> playerMap = new HashMap<>();
        for (Player player : playerList) {
            playerMap.put(player.getName() + "+" + player.getSurname(), player);
        }
        for (String playerLink : playerLinks) {
            Document content = null;
            try {
                content = Jsoup.parse(new URL(playerLink), 10000);
            } catch (Exception e1) {
                throw new VkException("Exception parsing url: " + playerLink, e1);
            }
            Elements headshots = content.getElementsByClass("vcard");
            for (Element headshot : headshots) {
                try {
                    Element profileUrl = headshot.getElementsByClass("url").first();
                    String href = profileUrl.attr("href");
                    if (profileUrl != null) {
                        Document contentP = Jsoup.parse(
                                new URL("http://www.tottenhamhotspur.com" + href),
                                10000);
                        Player player = new Player();
                        String[] nameSurname = contentP.getElementsByTag("h1").first().text().split(" ");
                        player.setName(nameSurname[0]);
                        player.setSurname(nameSurname[1]);
                        Element playerInfoEl = contentP
                                .getElementsByClass("player-info").first();
                        Elements dlEls = playerInfoEl.getElementsByTag("dl");

                        for (Element dlEl : dlEls) {
                            String key = dlEl.getElementsByTag("dt").first().text();
                            String value = dlEl.getElementsByTag("dd").first()
                                    .text();

                            if (DATE_OF_BIRTH.equals(key)) {
                                Date dateOfBirth = formatter.parse(value, new ParsePosition(0));
                                player.setDateOfBirth(dateOfBirth);
                            } else if (POSITION.equals(key)) {
                                player.setPosition(Position.getByString(value));
                            } else if (JOINED_SPURS.equals(key)) {
                                Date joinedSpurs = formatter.parse(value, new ParsePosition(0));
                                player.setDateJoined(joinedSpurs);
                            } else if (PREVIOUS_CLUBS.equals(key)) {
                                player.setPreviousClubs(value);
                            } else if (SQUAD_NUMBER.equals(key)) {
                                player.setSquadNumber(Byte.valueOf(value));
                            }
                        }
                        Player existingPlayer = playerMap.get(join(player.getName(), player.getSurname()));
                        if (existingPlayer == null) {
                            playerService.save(player);
                        } else {
                            player.setId(existingPlayer.getId());
                            playerService.update(player);
                        }
                    }
                } catch (Exception e) {
                    //log and proceed
                    LOGGER.error("Error loading player's profile.", e);
                }
            }

        }
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    private static String join (String name, String surname) {
        return name + "+" + surname;
    }
    
    public static final void main (String[] args) throws ParseException {
        System.out.println(formatter.parse("27 June 2007"));
    }
}

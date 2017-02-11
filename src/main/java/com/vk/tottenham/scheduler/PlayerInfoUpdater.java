package com.vk.tottenham.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.core.model.Position;
import com.vk.tottenham.footballapi.FootballApiGateway;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.CompetitionsResponse;
import com.vk.tottenham.footballapi.model.SquadResponse;
import com.vk.tottenham.mybatis.service.PlayerService;
import com.vk.tottenham.utils.CompSeasonUtil;

public class PlayerInfoUpdater extends SchedulerBase {

    public static SimpleDateFormat formatter = new SimpleDateFormat(
            "dd MMMM yyyy", Locale.US);

    static {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private PlayerService playerService;

    @Autowired
    @Qualifier("footballApiGateway")
    private FootballApiGateway footballApiGateway;

    @Override
    public void execute() {
        CompetitionsResponse competitionsResponse = footballApiGateway.getCompetitions();
        String compSeason = CompSeasonUtil.getCompSeason(competitionsResponse, CompetitionName.PREMIER_LEAGUE);
        SquadResponse squadResponse = footballApiGateway.getSquad(compSeason);
        for (com.vk.tottenham.footballapi.model.Player fapiPlayer : squadResponse.getPlayers()) {
            Player player = new Player();
            player.setDateJoined(fapiPlayer.getJoinDate().getLabel());
            player.setDateOfBirth(fapiPlayer.getBirth().getDate().getLabel());
            player.setName(fapiPlayer.getName().getFirst());
            player.setPosition(Position.getByString(fapiPlayer.getInfo().getPosition()));
            player.setSquadNumber((byte)fapiPlayer.getInfo().getShirtNum());
            player.setSurname(fapiPlayer.getName().getLast());
            Player existing = playerService.findNameAndSurname(player);
            if (existing == null) {
                playerService.save(player);
            } else {
                player.setId(existing.getId());
                playerService.update(player);
            }
        }
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public static final void main (String[] args) throws ParseException {
        System.out.println(formatter.parse("27 June 2007"));
    }
}

package com.vk.tottenham.utils;

import java.util.Calendar;

import com.vk.tottenham.footballapi.model.Competition;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.footballapi.model.CompetitionSeason;
import com.vk.tottenham.footballapi.model.CompetitionsResponse;

public class CompSeasonUtil {
    public static String getCompSeason (CompetitionsResponse competitionsResponse, CompetitionName competition) {
        for (Competition competitionEntry : competitionsResponse.getContent()) {
            if ((int)competitionEntry.getId() == Integer.valueOf(competition.comp())) {
                for (CompetitionSeason compSeasonEntry : competitionEntry.getCompSeasons()) {
                    if (compSeasonEntry.getLabel().startsWith(getSeasonStart())) {
                        return String.valueOf((int)compSeasonEntry.getId());
                    }
                }
            }
        }
        return null;
    }

    public static String getSeasonStart() {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.MONTH) > 6) {
            return String.valueOf(now.get(Calendar.YEAR));
        }
        return String.valueOf(now.get(Calendar.YEAR) - 1);
    }

    public static String getSeasonFull() {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.MONTH) > 6) {
            return String.valueOf(now.get(Calendar.YEAR) + "/" + (now.get(Calendar.YEAR) + 1));
        }
        return String.valueOf(now.get(Calendar.YEAR) - 1) + "/" + now.get(Calendar.YEAR);
    }
}

package com.vk.tottenham.footballapi.model;

import java.util.List;

public class Table {
    private int gameWeek;
    private List<TableEntry> entries;

    public int getGameWeek() {
        return gameWeek;
    }

    public void setGameWeek(int gameWeek) {
        this.gameWeek = gameWeek;
    }

    public List<TableEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TableEntry> entries) {
        this.entries = entries;
    }
}

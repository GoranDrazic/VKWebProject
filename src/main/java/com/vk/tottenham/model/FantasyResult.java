package com.vk.tottenham.model;

public class FantasyResult {
    private int id;//9304697
    private String entryName;//"ForsakenCowboy"
    private int eventTotal;//60
    private String playerName;//"Vadim Kostitsyn"
    private Movement movement;//"up";//TODO: enum
    private boolean ownEntry;//false;
    private int rank;// 1;
    private int lastRank;//: 2;
    private int rankSort;//: 1;
    private int total;//: 997;
    private int entry;//: 78084;
    private int league;// 62711;
    private int startEvent;//: 1;
    private int stopEvent;//: 38;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEntryName() {
        return entryName;
    }
    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
    public int getEventTotal() {
        return eventTotal;
    }
    public void setEventTotal(int eventTotal) {
        this.eventTotal = eventTotal;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public Movement getMovement() {
        return movement;
    }
    public void setMovement(Movement movement) {
        this.movement = movement;
    }
    public boolean isOwnEntry() {
        return ownEntry;
    }
    public void setOwnEntry(boolean ownEntry) {
        this.ownEntry = ownEntry;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public int getLastRank() {
        return lastRank;
    }
    public void setLastRank(int lastRank) {
        this.lastRank = lastRank;
    }
    public int getRankSort() {
        return rankSort;
    }
    public void setRankSort(int rankSort) {
        this.rankSort = rankSort;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getEntry() {
        return entry;
    }
    public void setEntry(int entry) {
        this.entry = entry;
    }
    public int getLeague() {
        return league;
    }
    public void setLeague(int league) {
        this.league = league;
    }
    public int getStartEvent() {
        return startEvent;
    }
    public void setStartEvent(int startEvent) {
        this.startEvent = startEvent;
    }
    public int getStopEvent() {
        return stopEvent;
    }
    public void setStopEvent(int stopEvent) {
        this.stopEvent = stopEvent;
    }

    @Override
    public String toString() {
        return "FantasyResult [id=" + id + ", entryName=" + entryName
                + ", eventTotal=" + eventTotal + ", playerName=" + playerName
                + ", movement=" + movement + ", ownEntry=" + ownEntry + ", rank="
                + rank + ", lastRank=" + lastRank + ", rankSort=" + rankSort
                + ", total=" + total + ", entry=" + entry + ", league=" + league
                + ", startEvent=" + startEvent + ", stopEvent=" + stopEvent + "]";
    }
}

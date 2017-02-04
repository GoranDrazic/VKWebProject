package com.vk.tottenham.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GameWeek {
    private int id;//: 1,
    private String name;//: "Gameweek 1",
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssX")
    private Date deadlineTime;//: "2016-08-13T10:30:00Z",
    private int averageEntryScore;//: 44,
    private boolean finished;//: true,
    private boolean dataChecked;//: true,
    private int highestScoringEntry;//: 719297,
    private long deadlineTimeEpoch;//: 1471084200,
    private int deadlineTimeGameOffset;//: 3600,
    private String deadlineTimeFormatted;//: "13 Aug 11:30",
    private int highestScore;//: 104,
    private boolean isPrevious;//: false,
    private boolean isCurrent;//: false,
    private boolean isNext;//: false
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDeadlineTime() {
        return deadlineTime;
    }
    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }
    public int getAverageEntryScore() {
        return averageEntryScore;
    }
    public void setAverageEntryScore(int averageEntryScore) {
        this.averageEntryScore = averageEntryScore;
    }
    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public boolean isDataChecked() {
        return dataChecked;
    }
    public void setDataChecked(boolean dataChecked) {
        this.dataChecked = dataChecked;
    }
    public int getHighestScoringEntry() {
        return highestScoringEntry;
    }
    public void setHighestScoringEntry(int highestScoringEntry) {
        this.highestScoringEntry = highestScoringEntry;
    }
    public long getDeadlineTimeEpoch() {
        return deadlineTimeEpoch;
    }
    public void setDeadlineTimeEpoch(long deadlineTimeEpoch) {
        this.deadlineTimeEpoch = deadlineTimeEpoch;
    }
    public int getDeadlineTimeGameOffset() {
        return deadlineTimeGameOffset;
    }
    public void setDeadlineTimeGameOffset(int deadlineTimeGameOffset) {
        this.deadlineTimeGameOffset = deadlineTimeGameOffset;
    }
    public String getDeadlineTimeFormatted() {
        return deadlineTimeFormatted;
    }
    public void setDeadlineTimeFormatted(String deadlineTimeFormatted) {
        this.deadlineTimeFormatted = deadlineTimeFormatted;
    }
    public int getHighestScore() {
        return highestScore;
    }
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
    public boolean isPrevious() {
        return isPrevious;
    }
    public void setPrevious(boolean isPrevious) {
        this.isPrevious = isPrevious;
    }
    public boolean isCurrent() {
        return isCurrent;
    }
    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
    public boolean isNext() {
        return isNext;
    }
    public void setNext(boolean isNext) {
        this.isNext = isNext;
    }
    @Override
    public String toString() {
        return "GameWeek [id=" + id + ", name=" + name + ", deadlineTime="
                + deadlineTime + ", averageEntryScore=" + averageEntryScore
                + ", finished=" + finished + ", dataChecked=" + dataChecked
                + ", highestScoringEntry=" + highestScoringEntry
                + ", deadlineTimeEpoch=" + deadlineTimeEpoch
                + ", deadlineTimeGameOffset=" + deadlineTimeGameOffset
                + ", deadlineTimeFormatted=" + deadlineTimeFormatted
                + ", highestScore=" + highestScore + ", isPrevious=" + isPrevious
                + ", isCurrent=" + isCurrent + ", isNext=" + isNext + "]";
    }
}

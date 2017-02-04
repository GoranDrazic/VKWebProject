package com.vk.tottenham.footballapi.model;

public class PlayerStatistics {
    private Owner owner;
    private double rank;
    private String name;
    private double value;
    private String description;
    private AdditionalInfo additionalInfo;
    public Owner getOwner() {
        return owner;
    }
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    public double getRank() {
        return rank;
    }
    public void setRank(double rank) {
        this.rank = rank;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }  
}

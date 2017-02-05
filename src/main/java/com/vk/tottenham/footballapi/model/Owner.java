package com.vk.tottenham.footballapi.model;

public class Owner {
    private double playerId;
    private Info info;
    private NationalTeam nationalTeam;
    private Team currentTeam;
    private boolean active;
    private DateOfBirth birth;
    private String age;
    private Name name;
    private double id;
    private AltIds altIds;
    public double getPlayerId() {
        return playerId;
    }
    public void setPlayerId(double playerId) {
        this.playerId = playerId;
    }
    public Info getInfo() {
        return info;
    }
    public void setInfo(Info info) {
        this.info = info;
    }
    public NationalTeam getNationalTeam() {
        return nationalTeam;
    }
    public void setNationalTeam(NationalTeam nationalTeam) {
        this.nationalTeam = nationalTeam;
    }
    public Team getCurrentTeam() {
        return currentTeam;
    }
    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public DateOfBirth getBirth() {
        return birth;
    }
    public void setBirth(DateOfBirth birth) {
        this.birth = birth;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public Name getName() {
        return name;
    }
    public void setName(Name name) {
        this.name = name;
    }
    public double getId() {
        return id;
    }
    public void setId(double id) {
        this.id = id;
    }
    public AltIds getAltIds() {
        return altIds;
    }
    public void setAltIds(AltIds altIds) {
        this.altIds = altIds;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(playerId);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Owner other = (Owner) obj;
        if (Double.doubleToLongBits(playerId) != Double
                .doubleToLongBits(other.playerId))
            return false;
        return true;
    }
}

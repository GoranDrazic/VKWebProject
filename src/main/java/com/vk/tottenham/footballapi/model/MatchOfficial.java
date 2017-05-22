package com.vk.tottenham.footballapi.model;

public class MatchOfficial {
    private int matchOfficialId;
    private String role;
    private DateOfBirth birth;
    private Name name;
    private int id;

    public int getMatchOfficialId() {
        return matchOfficialId;
    }

    public void setMatchOfficialId(int matchOfficialId) {
        this.matchOfficialId = matchOfficialId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DateOfBirth getBirth() {
        return birth;
    }

    public void setBirth(DateOfBirth birth) {
        this.birth = birth;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

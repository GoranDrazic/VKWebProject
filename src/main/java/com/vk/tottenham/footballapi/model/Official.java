package com.vk.tottenham.footballapi.model;

public class Official {
    private int officialId;
    private String role;
    private boolean active;
    private DateOfBirth birth;
    private String age;
    private Name name;
    private int id;
    private AltIds altIds;
    public int getOfficialId() {
        return officialId;
    }
    public void setOfficialId(int officialId) {
        this.officialId = officialId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
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
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public AltIds getAltIds() {
        return altIds;
    }
    public void setAltIds(AltIds altIds) {
        this.altIds = altIds;
    }
}

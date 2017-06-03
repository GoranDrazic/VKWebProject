package com.vk.tottenham.instagram.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntryData {
    private List<ProfilePage> profilePage;

    @JsonProperty("ProfilePage")
    public List<ProfilePage> getProfilePage() {
        return profilePage;
    }

    public void setProfilePage(List<ProfilePage> profilePage) {
        this.profilePage = profilePage;
    }
}

package com.vk.tottenham.core.model;

import java.util.Date;

public class Player {
    private int id;
    private String name;
    private String surname;
    private Date dateOfBirth;
    private Position position;
    private Date dateJoined;
    private String previousClubs;
    private byte squadNumber;
    private String instagram;
    private String russianNameNom;
    private String statsPhoto;

    public Player() {
        super();
    }

    public Player(String name, String surname) {
        super();
        this.name = name;
        this.surname = surname;
    }

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getPreviousClubs() {
        return previousClubs;
    }

    public void setPreviousClubs(String previousClubs) {
        this.previousClubs = previousClubs;
    }

    public byte getSquadNumber() {
        return squadNumber;
    }

    public void setSquadNumber(byte squadNumber) {
        this.squadNumber = squadNumber;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getRussianNameNom() {
        return russianNameNom;
    }

    public void setRussianNameNom(String russianNameNom) {
        this.russianNameNom = russianNameNom;
    }

    public String getStatsPhoto() {
        return statsPhoto;
    }

    public void setStatsPhoto(String statsPhoto) {
        this.statsPhoto = statsPhoto;
    }
}
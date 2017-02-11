package com.vk.tottenham.core.model;

public enum Position {
    Goalkeeper, Defender, Midfielder, Forward;
    public static Position getByString(String s) {
        switch (s) {
        case "G":
            return Goalkeeper;
        case "D":
            return Defender;
        case "M":
            return Midfielder;
        case "F":
            return Forward;
        }
        return null;
    }
}

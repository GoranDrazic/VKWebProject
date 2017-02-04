package com.vk.tottenham.core.model;

public enum Position {
    AttackingMidfielder, Goalkeeper, Defender, Midfielder, Striker, Forward;
    public static Position getByString(String s) {
        switch (s) {
        case "Attacking midfielder":
            return AttackingMidfielder;
        case "Goalkeeper":
            return Goalkeeper;
        case "Defender":
            return Defender;
        case "Midfielder":
            return Midfielder;
        case "Striker":
            return Striker;
        case "Forward":
            return Forward;
        }
        return null;
    }
}

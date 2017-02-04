package com.vk.tottenham.model;

import java.util.List;

public class Standings {
    private List<FantasyResult> results;

    public List<FantasyResult> getResults() {
        return results;
    }

    public void setResults(List<FantasyResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Standings [results=" + results + "]";
    }
}

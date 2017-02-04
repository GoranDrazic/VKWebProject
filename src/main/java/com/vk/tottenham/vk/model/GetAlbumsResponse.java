package com.vk.tottenham.vk.model;

import java.util.List;

public class GetAlbumsResponse {
    private int count;
    private List<Album> items;
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public List<Album> getItems() {
        return items;
    }
    public void setItems(List<Album> items) {
        this.items = items;
    }
}

package com.vk.tottenham.vk.model;

import java.util.List;

public class GetPhotosResponse {
    private int count;
    private List<Photo> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Photo> getItems() {
        return items;
    }

    public void setItems(List<Photo> items) {
        this.items = items;
    }
}

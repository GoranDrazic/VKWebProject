package com.vk.tottenham.vk.model;

import java.util.List;

public class GetPostsResponse {
    private int count;
    private List<Post> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Post> getItems() {
        return items;
    }

    public void setItems(List<Post> items) {
        this.items = items;
    }
}

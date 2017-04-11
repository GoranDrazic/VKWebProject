package com.vk.tottenham.vk.model;

import java.util.List;

public class GetHistoryResponse {
    private int count;
    private List<Message> items;
    private int inRead;
    private int outRead;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Message> getItems() {
        return items;
    }

    public void setItems(List<Message> items) {
        this.items = items;
    }

    public int getInRead() {
        return inRead;
    }

    public void setInRead(int inRead) {
        this.inRead = inRead;
    }

    public int getOutRead() {
        return outRead;
    }

    public void setOutRead(int outRead) {
        this.outRead = outRead;
    }
}

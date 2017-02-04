package com.vk.tottenham.footballapi.model;

public class PageInfo {
    private int page;
    private int numPages;
    private int pageSize;
    private int numEntries;
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getNumPages() {
        return numPages;
    }
    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getNumEntries() {
        return numEntries;
    }
    public void setNumEntries(int numEntries) {
        this.numEntries = numEntries;
    }
}

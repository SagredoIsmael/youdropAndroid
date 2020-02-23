package com.youdrop.youdrop.data.results;

import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.Publication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pau on 24/10/17.
 */

public class PublicationsResult implements Serializable {

    List<Publication> items;
    int page, limit, pages;

    public PublicationsResult(List<Publication> items) {
        this.items = items;
    }

    public List<Publication> getItems() {
        return items;
    }

    public void setItems(List<Publication> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

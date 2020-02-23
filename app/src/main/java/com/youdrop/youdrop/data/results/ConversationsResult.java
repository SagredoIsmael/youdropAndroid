package com.youdrop.youdrop.data.results;

import com.youdrop.youdrop.data.Conversation;
import com.youdrop.youdrop.data.Notification;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pau on 24/10/17.
 */

public class ConversationsResult implements Serializable {

    List<Conversation> items;
    int page, limit, pages;

    public ConversationsResult(List<Conversation> items) {
        this.items = items;
        this.page = 1;
    }

    public List<Conversation> getItems() {
        return items;
    }

    public void setItems(List<Conversation> items) {
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

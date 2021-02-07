package com.svnlib.distrodb.net.msg;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private final String       title;
    private final Serializable payload;

    protected Message(final String title, final Serializable payload) {
        this.title = title;
        this.payload = payload;
    }

    public String getTitle() {
        return this.title;
    }

    public Object getPayload() {
        return this.payload;
    }

    @Override
    public String toString() {
        return "Message{" +
               "title='" + this.title + '\'' +
               ", payload=" + this.payload +
               '}';
    }

}

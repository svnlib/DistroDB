package com.svnlib.distrodb.node.operation;

import java.util.UUID;

public class InsertOperation extends Operation {

    public static final String TYPE = "INSERT";

    private InsertOperation(final String payload) {
        super(TYPE);
        this.uuid = UUID.randomUUID();
        this.payload = payload;
    }

    public static Operation with(final String payload) {
        return new InsertOperation(payload);
    }

}

package com.svnlib.distrodb.node.operation;

import java.util.UUID;

public class GetOperation extends Operation {

    public static final String TYPE = "GET";

    private GetOperation(final String dbName, final UUID uuid) {
        super(TYPE, dbName);
        this.uuid = uuid;
    }

    public static Operation with(final String dbName, final String uuid) {
        return new GetOperation(dbName, UUID.fromString(uuid));
    }

}

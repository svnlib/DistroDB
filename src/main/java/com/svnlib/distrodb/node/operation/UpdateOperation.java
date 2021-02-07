package com.svnlib.distrodb.node.operation;

import java.util.UUID;

public class UpdateOperation extends Operation {

    public static final String TYPE = "UPDATE";

    private UpdateOperation(final String dbName, final UUID uuid, final String payload) {
        super(TYPE, dbName);
        this.uuid = uuid;
        this.payload = payload;
    }

    public static Operation with(final String dbName, final String uuid, final String payload) {
        return new UpdateOperation(dbName, UUID.fromString(uuid), payload);
    }

}

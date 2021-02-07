package com.svnlib.distrodb.node.operation;

import java.util.UUID;

public class DeleteOperation extends Operation {

    public static final String TYPE = "DELETE";

    private DeleteOperation(final String dbName, final UUID uuid) {
        super(TYPE, dbName);
        this.uuid = uuid;
    }

    public static Operation with(final String dbName, final String uuid) {
        return new DeleteOperation(dbName, UUID.fromString(uuid));
    }

}

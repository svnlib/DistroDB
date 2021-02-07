package com.svnlib.distrodb.node.operation;

import java.util.UUID;

public class DeleteOperation extends Operation {

    public static final String TYPE = "DELETE";

    private DeleteOperation(final UUID uuid) {
        super(TYPE);
        this.uuid = uuid;
    }

    public static Operation with(final String uuid) {
        return new DeleteOperation(UUID.fromString(uuid));
    }

}

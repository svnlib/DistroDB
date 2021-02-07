package com.svnlib.distrodb.node.operation;

import java.util.UUID;

public class UpdateOperation extends Operation {

    public static final String TYPE = "UPDATE";

    private UpdateOperation(final UUID uuid, final String payload) {
        super(TYPE);
        this.uuid = uuid;
        this.payload = payload;
    }

    public static Operation with(final String uuid, final String payload) {
        return new UpdateOperation(UUID.fromString(uuid), payload);
    }

}

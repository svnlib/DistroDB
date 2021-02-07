package com.svnlib.distrodb.node.operation;

import java.util.UUID;

public class GetOperation extends Operation {

    public static final String TYPE = "GET";

    private GetOperation(final UUID uuid) {
        super(TYPE);
        this.uuid = uuid;
    }

    public static Operation with(final String uuid) {
        return new GetOperation(UUID.fromString(uuid));
    }

}

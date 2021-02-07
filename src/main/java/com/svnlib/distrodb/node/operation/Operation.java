package com.svnlib.distrodb.node.operation;

import java.io.Serializable;
import java.util.UUID;

/**
 * The base class of all operations.
 */
public class Operation implements Serializable {

    private final String type;
    protected     UUID   uuid    = null;
    protected     String payload = null;

    protected Operation(final String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getPayload() {
        return this.payload;
    }

    @Override
    public String toString() {
        return "Operation{" +
               "type=" + this.type + ", " +
               "uuid=" + this.uuid + ", " +
               "payload=" + this.payload +
               "}";
    }

}

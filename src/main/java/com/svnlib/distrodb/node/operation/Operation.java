package com.svnlib.distrodb.node.operation;

import java.io.Serializable;
import java.util.UUID;

/**
 * The base class of all operations.
 */
public class Operation implements Serializable {

    protected UUID   uuid    = null;
    protected String payload = null;
    private   String type;
    private   String dbName;

    protected Operation(final String type, final String dbName) {
        this.type = type;
        this.dbName = dbName;
    }

    public String getType() {
        return this.type;
    }

    public String getDbName() {
        return this.dbName;
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

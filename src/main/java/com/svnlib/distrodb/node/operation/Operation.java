package com.svnlib.distrodb.node.operation;

import java.io.Serializable;
import java.util.UUID;

public class Operation implements Serializable {

    private final UUID uuid;

    protected Operation(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
               "uuid=" + this.uuid +
               '}';
    }

}

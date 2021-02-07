package com.svnlib.distrodb.node.operation;

public class DeleteDbOperation extends Operation {

    public static final String TYPE = "DELETE_DB";

    protected DeleteDbOperation(final String dbName) {
        super(TYPE, dbName);
    }

    public static Operation with(final String dbName) {
        return new DeleteDbOperation(dbName);
    }

}

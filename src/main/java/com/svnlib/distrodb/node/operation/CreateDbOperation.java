package com.svnlib.distrodb.node.operation;

public class CreateDbOperation extends Operation {

    public static final String TYPE = "CREATE_DB";

    protected CreateDbOperation(final String dbName) {
        super(TYPE, dbName);
    }

    public static Operation with(final String dbName) {
        return new CreateDbOperation(dbName);
    }

}

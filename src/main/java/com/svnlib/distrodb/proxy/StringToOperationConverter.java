package com.svnlib.distrodb.proxy;

import com.svnlib.distrodb.node.operation.*;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Converts a String into an operation.
 */
public class StringToOperationConverter {

    private static final Pattern UUID_PATTERN    = Pattern.compile(
            "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b",
            CASE_INSENSITIVE);
    private static final Pattern DB_NAME_PATTERN = Pattern.compile("([a-z\\-_]+)", CASE_INSENSITIVE);

    private final String[] splits;

    public StringToOperationConverter(final String msg) {
        this.splits = msg.split(" ");
    }

    /**
     * Performs the conversion
     *
     * @return the converted {@link Operation}
     *
     * @throws IllegalArgumentException if the given string is not convertible.
     */
    public Operation convert() throws IllegalArgumentException {
        final String type = this.splits[0];

        switch (type.toUpperCase()) {
            case GetOperation.TYPE -> {
                checkLength(2);
                checkUuid();
                return GetOperation.with(this.splits[1]);
            }
            case InsertOperation.TYPE -> {
                checkLength(2);
                return InsertOperation.with(this.splits[1]);
            }
            case UpdateOperation.TYPE -> {
                checkLength(3);
                checkUuid();
                return UpdateOperation.with(this.splits[1], this.splits[2]);
            }
            case DeleteOperation.TYPE -> {
                checkLength(2);
                checkUuid();
                return InsertOperation.with(this.splits[1]);
            }
            default -> throw new IllegalArgumentException("Type not found!");
        }
    }

    private void checkLength(final int length) {
        if (this.splits.length < length) {
            throw new IllegalArgumentException(
                    "The request has to consist out of " + length + " parts but got " + this.splits.length);
        }
    }

    private void checkUuid() {
        if (!UUID_PATTERN.matcher(this.splits[1]).matches()) {
            throw new IllegalArgumentException("Invalid UUID");
        }
    }

}

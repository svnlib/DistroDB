package com.svnlib.distrodb.net.msg;

import com.svnlib.distrodb.node.operation.Operation;

import java.io.Serializable;

public class OperationMessage extends Message {

    public static final String  TITLE = "OPERATION";
    private final       boolean shouldRedistribute;

    protected OperationMessage(final String title, final Serializable payload, final boolean shouldRedistribute) {
        super(title, payload);
        this.shouldRedistribute = shouldRedistribute;
    }

    /**
     * Creates an {@link OperationMessage} that contains the operation it self and a flag that indicates whether the
     * message should be redistributed to all the other nodes.
     *
     * @param operation    the operation to be transmitted
     * @param redistribute whether the receiver should redistribute the message
     *
     * @return the created {@link OperationMessage}
     */
    public static OperationMessage create(final Operation operation, final boolean redistribute) {
        return new OperationMessage(TITLE, operation, redistribute);
    }

    @Override
    public Operation getPayload() {
        return (Operation) super.getPayload();
    }

    /**
     * Returns whether the receiver of the message should send the message to all the other nodes.
     *
     * @return {@code true} if the receiver should send the message to all nodes except itself or {@code false} if the
     * receiver should not.
     */
    public boolean shouldRedistribute() {
        return this.shouldRedistribute;
    }

}

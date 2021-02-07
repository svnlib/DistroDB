package com.svnlib.distrodb.net;

import com.svnlib.distrodb.net.msg.OperationMessage;
import com.svnlib.distrodb.node.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Responsible for keeping an open connection to the corresponding node and sending the queued {@link Operation}s to the
 * node.
 */
public class OperationRelay extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRelay.class);

    private final BlockingQueue<OperationMessage> operationMessageQueue = new LinkedBlockingQueue<>();
    private final Connection                      connection;

    /**
     * Creates the OperationRelay and directly begins operating on the given {@link Connection}.
     *
     * @param connection the connection to use
     */
    public OperationRelay(final Connection connection) {
        this.connection = connection;
        this.setName("OperationRelay - " + connection.getHost());
        this.start();
        LOGGER.debug("OperationRelay created for {}.", this.connection);
    }

    /**
     * Queues a given operation which will be send as soon as the connection is free.
     *
     * @param operation the operation to be queued
     */
    public void send(final OperationMessage operation) {
        try {
            this.operationMessageQueue.put(operation);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        LOGGER.debug("The OperationRelay was interrupted. Sending last operations if any...");
        super.interrupt();
    }

    @Override
    public void run() {

        while (!this.isInterrupted() || !this.operationMessageQueue.isEmpty()) {
            try {
                final OperationMessage operation = this.operationMessageQueue.take();
                sendOperation(operation);
            } catch (final InterruptedException ignored) {
            }
        }
        LOGGER.debug("Shutting down the OperationRelay and closing connection.");
        this.connection.close();
    }

    @Override
    public String toString() {
        return "OperationRelay{" + this.connection.toString() + "}";
    }

    /**
     * Tries to send the given operation over the OperationRelays connection.
     *
     * @param operationMessage the operation to be send
     */
    private void sendOperation(final OperationMessage operationMessage) {
        try {
            this.connection.sendObject(operationMessage);
        } catch (final IOException e) {
            LOGGER.error("Could not send operation to {}", this.connection);
            e.printStackTrace();
        }
    }

}

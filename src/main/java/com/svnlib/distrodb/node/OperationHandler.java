package com.svnlib.distrodb.node;

import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.net.msg.OperationMessage;
import com.svnlib.distrodb.node.operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Handles all operations received form the given connection.
 */
public class OperationHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationHandler.class);

    private final Connection               connection;
    private final NodeOperationDistributor operationDistributor;

    /**
     * Create a OperationHandler responsible for {@code connection}. The resulting {@link Thread} will be started
     * directly.
     *
     * @param connection           the connection the OperationHandler should listen on
     * @param operationDistributor the {@link NodeOperationDistributor} that distributes messages to all the other
     *                             hosts
     */
    public OperationHandler(final Connection connection, final NodeOperationDistributor operationDistributor) {
        this.connection = connection;
        this.operationDistributor = operationDistributor;
        this.setName("OperationHandler");
        this.start();
    }

    @Override
    public void run() {
        while (this.connection.isConnected()) {
            try {
                final Object o = this.connection.readObject();
                if (!(o instanceof OperationMessage)) {
                    LOGGER.error("Received unknown object {}", o);
                    continue;
                }
                handleOperation((OperationMessage) o);
            } catch (final IOException e) {
                LOGGER.error("Could not receive operation from {}", this.connection);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the received operation.
     *
     * @param operationMessage the received operation
     */
    private void handleOperation(final OperationMessage operationMessage) {
        final Operation operation = operationMessage.getPayload();

        if (operationMessage.shouldRedistribute()) {
            this.operationDistributor.distributeOperation(OperationMessage.create(operation, false));
        }

        LOGGER.info("Handling {}", operation);

        final String uuid    = operation.getUuid().toString();
        final String payload = operation.getPayload();

        switch (operation.getType()) {
            case InsertOperation.TYPE, UpdateOperation.TYPE -> DataStore.put(uuid, payload);
            case DeleteOperation.TYPE -> DataStore.remove(uuid);
            case GetOperation.TYPE -> DataStore.get(uuid);
        }

        LOGGER.info("Items in store: {}", DataStore.size());
    }

}

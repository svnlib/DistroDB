package com.svnlib.distrodb.node;

import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.node.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OperationHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationHandler.class);

    private final Connection connection;

    public OperationHandler(final Connection connection) {
        this.connection = connection;
        this.setName("OperationHandler");
        this.start();
    }

    @Override
    public void run() {
        while (this.connection.isConnected()) {
            try {
                final Object o = this.connection.readObject();
                if (!(o instanceof Operation)) {
                    LOGGER.error("Received unknown object {}", o);
                    continue;
                }
                handleOperation((Operation) o);
            } catch (final IOException e) {
                LOGGER.error("Could not receive operation from {}", this.connection);
                e.printStackTrace();
            }
        }
    }

    private void handleOperation(final Operation operation) {
        LOGGER.info("Received {}", operation);
    }

}

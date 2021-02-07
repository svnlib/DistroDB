package com.svnlib.distrodb.proxy;

import com.svnlib.distrodb.net.AbstractOperationDistributor;
import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.net.SocketServerThread;
import com.svnlib.distrodb.net.msg.OperationMessage;
import com.svnlib.distrodb.node.operation.InsertOperation;
import com.svnlib.distrodb.node.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Responsible for handling all connections coming from outside the system.
 */
public class IncomingOperationReceiver extends SocketServerThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingOperationReceiver.class);

    private final AbstractOperationDistributor distributor;

    public IncomingOperationReceiver(final AbstractOperationDistributor distributor) throws IOException {
        super(5000, "IncomingOperationReceiver");
        this.distributor = distributor;
        this.start();
    }

    @Override
    protected void handleConnection(final Connection connection) {
        try {
            final String msg = connection.readString();

            try {
                final Operation operation = new StringToOperationConverter(msg).convert();
                if (operation.getType().equals(InsertOperation.TYPE)) {
                    connection.sendString(operation.getUuid().toString());
                }
                this.distributor.distributeOperation(OperationMessage.create(operation, true));
            } catch (final IllegalArgumentException exception) {
                connection.sendString(exception.getMessage());
                LOGGER.warn("Cannot convert {} into a valid operation! ({})", msg, exception.getMessage());
            }
        } catch (final IOException e) {
            LOGGER.error("Couldn't handle incoming operation.", e);
        } finally {
            connection.close();
        }
    }

}

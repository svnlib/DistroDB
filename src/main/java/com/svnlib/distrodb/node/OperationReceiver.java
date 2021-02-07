package com.svnlib.distrodb.node;

import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.net.SocketServerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Listens for new operations.
 */
public class OperationReceiver extends SocketServerThread {

    private static final Logger                   LOGGER = LoggerFactory.getLogger(OperationReceiver.class);
    private final        NodeOperationDistributor operationDistributor;

    public OperationReceiver(final NodeOperationDistributor operationDistributor) throws IOException {
        super(4444, "OperationReceiver");
        this.operationDistributor = operationDistributor;
        this.start();
    }

    @Override
    protected void handleConnection(final Connection connection) {
        new OperationHandler(connection, this.operationDistributor);
    }

}

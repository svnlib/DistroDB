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

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationReceiver.class);

    public OperationReceiver() throws IOException {
        super(4444, "OperationReceiver");
        this.start();
    }

    @Override
    protected void handleConnection(final Connection connection) {
        try {
            connection.close();
        } catch (final IOException e) {
            LOGGER.error("Error handling operation from {}.", connection);
            e.printStackTrace();
        }
    }

}

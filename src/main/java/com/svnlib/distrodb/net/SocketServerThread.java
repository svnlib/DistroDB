package com.svnlib.distrodb.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * A {@link Thread} subclass that directly begins to run after creation. This class creates a {@link ServerSocket} and
 * listens for new connections handled within the {@link #handleConnection(Connection)} method.
 */
public abstract class SocketServerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerThread.class);

    private final ServerSocket serverSocket;

    /**
     * Create a new SocketServerThread listening on the given port. The name will be used to identify the thread.
     *
     * @param port the port to listen on
     * @param name the name of the new created thread
     *
     * @throws IOException if a network error occurs.
     */
    public SocketServerThread(final int port, final String name) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.setName(name);
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.handleConnection(Connection.fromSocket(this.serverSocket.accept()));
            } catch (final IOException e) {
                LOGGER.error("Error accepting connection.", e);
                if (this.serverSocket.isClosed()) {
                    break;
                }
            }
        }
    }

    /**
     * This method is used to handle the incoming {@link Connection}s. Make sure to close the connection at the end.
     *
     * @param connection the connection to handle
     */
    protected abstract void handleConnection(Connection connection);

}

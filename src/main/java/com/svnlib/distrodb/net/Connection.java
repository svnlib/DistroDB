package com.svnlib.distrodb.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A {@link Socket} wrapper class to easily send and receive objects. It can either create a new {@link Socket} using a
 * given host and port or use a given {@link Socket}.
 *
 * @see #fromHost(String, int)
 * @see #fromSocket(Socket)
 */
public class Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

    private final String host;
    private final int    port;
    private final Socket socket;

    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream  objectInputStream;

    /**
     * Creates a {@link Connection} with the given host, port and {@link Socket}. The host and ip is used for logging.
     * The {@link Socket} is used to send objects.
     *
     * @param host   the host address
     * @param port   the port of the connection to the host
     * @param socket the actual socket connection
     *
     * @throws IOException if an network error occurs.
     */
    private Connection(final String host, final int port, final Socket socket) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = socket;
        this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
    }

    /**
     * Creates a {@link Connection} using the given host and port and creates a {@link Socket} used within this
     * connection.
     *
     * @param host the host to connect to
     * @param port the port of the host to use
     *
     * @return the established connection
     *
     * @throws IOException if a network error occurs.
     */
    public static Connection fromHost(final String host, final int port) throws IOException {
        final Connection connection = new Connection(host, port, new Socket(host, port));
        LOGGER.debug("Created {}", connection);
        return connection;
    }

    /**
     * Create a {@link Connection} using an established socket connection used within the connection.
     *
     * @param socket the already established socket connection
     *
     * @return the established connection.
     *
     * @throws IOException              if a network error occurs.
     * @throws IllegalArgumentException if the socket is not connected.
     */
    public static Connection fromSocket(final Socket socket) throws IOException, IllegalArgumentException {
        if (!socket.isConnected()) {
            throw new IllegalArgumentException(
                    "The provided socket is not connected! A connection can only be created using a connected socket.");
        }
        final Connection connection =
                new Connection(socket.getInetAddress().getHostAddress(), socket.getPort(), socket);
        LOGGER.debug("Created {}", connection);
        return connection;
    }

    /**
     * Closes the socket and this connection.
     */
    public void close() {
        LOGGER.debug("Closing {}", this);
        try {
            this.socket.close();
        } catch (final IOException e) {
            LOGGER.error("Error while closing {}", this);
            e.printStackTrace();
        }
    }

    /**
     * Sends an object via the socket connection.
     *
     * @param object the object to be send
     *
     * @return the connection to be used for further operations
     *
     * @throws IOException if a network error occurs.
     */
    public Connection sendObject(final Object object) throws IOException {
        checkConnected();
        LOGGER.debug("Sending {} to {}", object, this);
        this.objectOutputStream.writeObject(object);
        return this;
    }

    /**
     * Reads an object from the socket connection.
     *
     * @return the red object
     *
     * @throws IOException if a network error occurs.
     */
    public Object readObject() throws IOException {
        checkConnected();
        try {
            final Object object = this.objectInputStream.readObject();
            LOGGER.debug("Received {} from {}", object, this);
            return object;
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("", e);
        }
    }

    /**
     * Returns the host which this connection is connected to.
     *
     * @return the connected host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Returns the port which this connection is connected to.
     *
     * @return the connected port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Returns the connection state of this connection.
     *
     * @return {@code true} if the connection is connected via the socket and {@code false} if the connection is closed.
     */
    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    @Override
    public String toString() {
        return "Connection{" + this.host + ":" + this.port + "}";
    }

    /**
     * Used internally to check whether the connection is alive.
     */
    private void checkConnected() {
        if (!isConnected()) {
            throw new IllegalStateException(
                    "Operation on a Connection object without being connected. You have to be connected first.");
        }
    }

}

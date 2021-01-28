package com.svnlib.distrodb.coordinator;

import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.net.SocketServerThread;
import com.svnlib.distrodb.net.msg.TopologyRegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.svnlib.distrodb.net.msg.TopologyRegistrationMessage.EntityType.NODE;
import static com.svnlib.distrodb.net.msg.TopologyRegistrationMessage.EntityType.PROXY;
import static com.svnlib.distrodb.net.msg.TopologyRegistrationMessage.TITLE_CONNECT;

/**
 * Listens for registration messages of hosts joining or leaving the system.
 */
public class TopologyRegistrationReceiver extends SocketServerThread {

    private static final Logger          LOGGER = LoggerFactory.getLogger(TopologyRegistrationReceiver.class);
    private final        TopologyManager topologyManager;

    public TopologyRegistrationReceiver(final TopologyManager topologyManager) throws IOException {
        super(3333, "TopologyRegistrationReceiver");
        this.topologyManager = topologyManager;
        this.start();
    }

    @Override
    protected void handleConnection(final Connection connection) {
        try {
            final TopologyRegistrationMessage message = (TopologyRegistrationMessage) connection.readObject();
            connection.close();

            if (message.getPayload() == NODE) {
                if (TITLE_CONNECT.equals(message.getTitle())) {
                    this.topologyManager.nodeJoin(connection.getHost());
                } else {
                    this.topologyManager.nodeLeave(connection.getHost());
                }
            } else if (message.getPayload() == PROXY) {
                if (TITLE_CONNECT.equals(message.getTitle())) {
                    this.topologyManager.proxyJoin(connection.getHost());
                } else {
                    this.topologyManager.proxyLeave(connection.getHost());
                }
            }
        } catch (final IOException e) {
            LOGGER.info("Error handling topology change from {}.", connection);
            e.printStackTrace();
        }
    }

}

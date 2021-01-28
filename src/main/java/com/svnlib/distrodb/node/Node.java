package com.svnlib.distrodb.node;

import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.net.msg.TopologyRegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.svnlib.distrodb.ConfigStore.getConfig;
import static com.svnlib.distrodb.net.msg.TopologyRegistrationMessage.EntityType.NODE;

/**
 * The main class of the Node operation type. This operation type is responsible for storing the data and synchronising
 * it with the other nodes. It registers itself at the coordinator and will receive the operations from the proxies.
 */
public class Node {

    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

    /**
     * Boots up a Node instance.
     *
     * @throws IOException if a network error occurs.
     */
    public Node() throws IOException {
        new TopologyReceiver();
        new OperationReceiver();
        registerAtCoordinator();
    }

    /**
     * Registers at the coordinator.
     *
     * @throws IOException if a network error occurs.
     */
    private void registerAtCoordinator() throws IOException {
        LOGGER.info("Notifying coordinator over boot up.");
        Connection.fromHost(getConfig().COORDINATOR_HOST, 3333)
                  .sendObject(TopologyRegistrationMessage.connect(NODE))
                  .close();
    }

}

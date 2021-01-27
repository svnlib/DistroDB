package com.svnlib.distrodb.coordinator;

import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.net.msg.TopologyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to keep track of the topology. It stores the hosts of all available nodes and proxies in two lists and notifies
 * them over changes to the topology.
 */
public class TopologyManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopologyManager.class);

    private final List<String> proxies = new ArrayList<>();
    private final List<String> nodes   = new ArrayList<>();

    /**
     * A new node joined the system and will be added to the list.
     *
     * @param node the joining node's host
     */
    public void nodeJoin(final String node) {
        LOGGER.info("Node {} joined the system.", node);
        this.nodes.add(node);
        this.sendToAllHosts();
    }

    /**
     * A node left the system and will be removed from the list.
     *
     * @param node the leaving node's host
     */
    public void nodeLeave(final String node) {
        LOGGER.info("Node {} left the system.", node);
        this.nodes.remove(node);
        this.sendToAllHosts();
    }

    /**
     * A new proxy joined the system and will be added to the list.
     *
     * @param proxy the joining node's host
     */
    public void proxyJoin(final String proxy) {
        LOGGER.info("Proxy {} joined the system.", proxy);
        this.proxies.add(proxy);
        this.sendToAllHosts();
    }

    /**
     * A proxy left the system and will be removed from the list.
     *
     * @param proxy the leaving node's host
     */
    public void proxyLeave(final String proxy) {
        LOGGER.info("Proxy {} left the system.", proxy);
        this.proxies.remove(proxy);
        this.sendToAllHosts();
    }

    /**
     * Sends the current topology to all proxies and nodes in the system.
     */
    private void sendToAllHosts() {
        LOGGER.info("Notifying all host over changed topology ({}).", this.nodes);
        final TopologyMessage topologyMessage = TopologyMessage.create(this.nodes);

        for (final String node : this.nodes) {
            try {
                Connection.fromHost(node, 3333).sendObject(topologyMessage).close();
            } catch (final IOException e) {
                LOGGER.error("Could not notify node {} about changed topology.", node);
                e.printStackTrace();
            }
        }
        for (final String proxy : this.proxies) {
            try {
                Connection.fromHost(proxy, 3333).sendObject(topologyMessage).close();
            } catch (final IOException e) {
                LOGGER.error("Could not notify proxy {} about changed topology.", proxy);
                e.printStackTrace();
            }
        }
    }

}

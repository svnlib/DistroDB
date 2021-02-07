package com.svnlib.distrodb.proxy;

import com.svnlib.distrodb.net.Connection;
import com.svnlib.distrodb.net.TopologyReceiver;
import com.svnlib.distrodb.net.TopologyReceiver.TopologyChangeListener;
import com.svnlib.distrodb.net.msg.TopologyRegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.svnlib.distrodb.ConfigStore.getConfig;
import static com.svnlib.distrodb.net.msg.TopologyRegistrationMessage.EntityType.PROXY;

/**
 * The main class of the proxy operation type. This operation type is used to receive the operations from the client and
 * to distribute them to the nodes.
 */
public class Proxy implements TopologyChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Proxy.class);

    private final ProxyOperationDistributor distributor;

    public Proxy() throws IOException {
        new TopologyReceiver(this);
        this.distributor = new ProxyOperationDistributor();
        registerAtCoordinator();
    }

    @Override
    public void onTopologyChanged(final List<String> nodes) {
        this.distributor.updateTopology(nodes);
    }

    /**
     * Registers at the coordinator.
     *
     * @throws IOException if a network error occurs.
     */
    private void registerAtCoordinator() throws IOException {
        LOGGER.info("Notifying coordinator over boot up.");
        Connection.fromHost(getConfig().COORDINATOR_HOST, 3333)
                  .sendObject(TopologyRegistrationMessage.connect(PROXY))
                  .close();
    }

}

package com.svnlib.distrodb.coordinator;

import java.io.IOException;

/**
 * The main class of the coordinator operation type. This operation type is responsible of managing the topology and
 * notifying all host about changes to it.
 */
public class Coordinator {

    /**
     * Boot up the coordinator.
     *
     * @throws IOException if a network error occurs.
     */
    public Coordinator() throws IOException {
        final TopologyManager topologyManager = new TopologyManager();
        new TopologyRegistrationReceiver(topologyManager);
    }

}

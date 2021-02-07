package com.svnlib.distrodb.net;

import com.svnlib.distrodb.net.msg.OperationMessage;
import com.svnlib.distrodb.node.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Used to decide which node is responsible for the operation. It contains a list of all nodes and a map of the
 * corresponding {@link OperationRelay}s.
 */
public abstract class AbstractOperationDistributor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperationDistributor.class);

    private final Object lock = new Object();

    private final Map<String, OperationRelay> operationRelayMap = new HashMap<>();
    private       List<String>                nodes             = new ArrayList<>();

    /**
     * Distributes an {@link Operation} to the responsible node by adding it to the queue of the corresponding {@link
     * OperationRelay}.
     *
     * @param operationMessage the OperationMessage containing the operation to be distributed
     */
    public void distributeOperation(final OperationMessage operationMessage) {
        synchronized (this.lock) {
            final List<String> hosts = operationMessageToHosts(operationMessage, new ArrayList<>(this.nodes));
            hosts.forEach(host -> this.operationRelayMap.get(host).send(operationMessage));
        }
    }

    /**
     * Updates the list of available nodes. All unused {@link OperationRelay} are stopped and new ones are created.
     *
     * @param nodes the list of nodes that are part of the system
     */
    public void updateTopology(final List<String> nodes) {
        synchronized (this.lock) {
            LOGGER.info("Updating relays...");

            final List<String> leavingNodes =
                    this.nodes.stream().filter(node -> !nodes.contains(node)).collect(Collectors.toList());
            if (!leavingNodes.isEmpty()) {
                LOGGER.info("Nodes {} left the system", leavingNodes);
            }

            leavingNodes.forEach(node -> {
                final OperationRelay operationRelay = this.operationRelayMap.get(node);
                operationRelay.interrupt();
                this.operationRelayMap.remove(node);
            });

            final List<String> joiningNodes =
                    nodes.stream().filter(node -> !this.nodes.contains(node)).collect(Collectors.toList());
            if (!joiningNodes.isEmpty()) {
                LOGGER.info("Nodes {} joined the system", joiningNodes);
            }

            joiningNodes.forEach(node -> {
                try {
                    final OperationRelay operationRelay = new OperationRelay(Connection.fromHost(node, 4444));
                    this.operationRelayMap.put(node, operationRelay);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });

            this.nodes = nodes;
            LOGGER.info("Update done!");
        }
    }

    /**
     * Returns a list of all hosts that the given {@link OperationMessage} should be send to.
     *
     * @param operationMessage the operation about to be send
     *
     * @return the list of host, the given operation should be distributed to.
     */
    protected abstract List<String> operationMessageToHosts(final OperationMessage operationMessage,
                                                            List<String> knownHosts);

}

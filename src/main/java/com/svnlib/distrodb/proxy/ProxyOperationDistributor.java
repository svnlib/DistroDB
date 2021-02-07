package com.svnlib.distrodb.proxy;

import com.svnlib.distrodb.net.AbstractOperationDistributor;
import com.svnlib.distrodb.net.msg.OperationMessage;

import java.util.List;

public class ProxyOperationDistributor extends AbstractOperationDistributor {

    @Override
    protected List<String> operationMessageToHosts(final OperationMessage operationMessage,
                                                   final List<String> knownHosts) {
        if (operationMessage.shouldRedistribute() && knownHosts.size() > 0) {
            final int    idx  = operationMessage.hashCode() % knownHosts.size();
            final String node = knownHosts.get(idx);
            return List.of(node);
        } else {
            return knownHosts;
        }
    }

}

package com.svnlib.distrodb.node;

import com.svnlib.distrodb.net.AbstractOperationDistributor;
import com.svnlib.distrodb.net.msg.OperationMessage;

import java.util.List;

public class NodeOperationDistributor extends AbstractOperationDistributor {

    private final String localhost;

    public NodeOperationDistributor(final String localhost) {this.localhost = localhost;}

    @Override
    protected List<String> operationMessageToHosts(final OperationMessage operationMessage,
                                                   final List<String> knownHosts) {
        knownHosts.remove(this.localhost);
        return knownHosts;
    }

}

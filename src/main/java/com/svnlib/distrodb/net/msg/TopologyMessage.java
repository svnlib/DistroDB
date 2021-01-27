package com.svnlib.distrodb.net.msg;

import java.util.List;

public class TopologyMessage extends Message {

    public static final String TITLE = "TOPOLOGY";

    private TopologyMessage(final String[] nodes) {
        super(TITLE, nodes);
    }

    public static TopologyMessage create(final List<String> nodes) {
        return new TopologyMessage(nodes.toArray(new String[0]));
    }

    @Override
    public List<String> getPayload() {
        final String[] objects = (String[]) super.getPayload();
        return List.of(objects);
    }

}

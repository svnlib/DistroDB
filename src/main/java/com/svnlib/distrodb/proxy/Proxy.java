package com.svnlib.distrodb.proxy;

import com.svnlib.distrodb.net.TopologyReceiver;
import com.svnlib.distrodb.net.TopologyReceiver.TopologyChangeListener;

import java.io.IOException;
import java.util.List;

public class Proxy implements TopologyChangeListener {

    public Proxy() throws IOException {
        new TopologyReceiver(this);
    }

    @Override
    public void onTopologyChanged(final List<String> nodes) {

    }

}

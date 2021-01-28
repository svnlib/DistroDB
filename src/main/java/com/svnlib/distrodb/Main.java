package com.svnlib.distrodb;

import com.svnlib.distrodb.coordinator.Coordinator;
import com.svnlib.distrodb.node.Node;
import com.svnlib.distrodb.proxy.Proxy;

import java.io.IOException;

import static com.svnlib.distrodb.ConfigStore.Mode.*;
import static com.svnlib.distrodb.ConfigStore.getConfig;

public class Main {

    public static void main(final String[] args) throws IOException {

        if (getConfig().MODE == COORDINATOR) {
            new Coordinator();
        }
        if (getConfig().MODE == PROXY) {
            new Proxy();
        }
        if (getConfig().MODE == NODE) {
            new Node();
        }
    }

}

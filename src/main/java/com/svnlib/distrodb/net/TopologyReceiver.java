package com.svnlib.distrodb.net;

import com.svnlib.distrodb.net.msg.TopologyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Listens for topology update messages.
 */
public class TopologyReceiver extends SocketServerThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopologyReceiver.class);

    private final TopologyChangeListener listener;

    public TopologyReceiver(final TopologyChangeListener listener) throws IOException {
        super(3333, "TopologyReceiver");
        this.listener = listener;
        this.start();
    }

    @Override
    protected void handleConnection(final Connection connection) {
        try {
            final Object object = connection.readObject();
            if (object instanceof TopologyMessage) {
                final TopologyMessage msg   = (TopologyMessage) object;
                final List<String>    nodes = msg.getPayload();
                LOGGER.info("Received new topology ({})", nodes);
                this.listener.onTopologyChanged(nodes);
            } else {
                LOGGER.warn("Received unknown message ({}).", object);
            }
            connection.close();
        } catch (final IOException e) {
            LOGGER.info("Could not handle topology update.", e);
        }
    }

    public interface TopologyChangeListener {

        void onTopologyChanged(List<String> nodes);

    }

}

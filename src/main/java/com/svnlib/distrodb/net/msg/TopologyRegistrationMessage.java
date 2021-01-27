package com.svnlib.distrodb.net.msg;

public class TopologyRegistrationMessage extends Message {

    public static final String TITLE_CONNECT    = "CONNECT";
    public static final String TITLE_DISCONNECT = "DISCONNECT";

    private TopologyRegistrationMessage(final String title, final EntityType type) {
        super(title, type);
    }

    public static TopologyRegistrationMessage connect(final EntityType type) {
        return new TopologyRegistrationMessage(TITLE_CONNECT, type);
    }

    public static TopologyRegistrationMessage disconnect(final EntityType type) {
        return new TopologyRegistrationMessage(TITLE_DISCONNECT, type);
    }

    @Override
    public EntityType getPayload() {
        return (EntityType) super.getPayload();
    }

    public enum EntityType {
        NODE,
        PROXY
    }

}

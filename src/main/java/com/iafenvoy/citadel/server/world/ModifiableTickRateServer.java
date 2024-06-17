package com.iafenvoy.citadel.server.world;

public interface ModifiableTickRateServer {
    void setGlobalTickLengthMs(long msPerTick);

    long getMasterMs();

    default void resetGlobalTickLengthMs() {
        this.setGlobalTickLengthMs(-1);
    }
}

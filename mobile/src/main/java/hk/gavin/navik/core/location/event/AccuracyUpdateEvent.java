package hk.gavin.navik.core.location.event;

import hk.gavin.navik.core.location.NKLocationProvider;

public class AccuracyUpdateEvent {

    public final NKLocationProvider provider;
    public final double accuracy;

    public AccuracyUpdateEvent(NKLocationProvider provider, double accuracy) {
        this.provider = provider;
        this.accuracy = accuracy;
    }
}

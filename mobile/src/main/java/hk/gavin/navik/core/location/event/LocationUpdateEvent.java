package hk.gavin.navik.core.location.event;

import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;

public class LocationUpdateEvent {

    public final NKLocationProvider provider;
    public final NKLocation location;
    public final double accuracy;

    public LocationUpdateEvent(NKLocationProvider provider, NKLocation location, double accuracy) {
        this.provider = provider;
        this.location = location;
        this.accuracy = accuracy;
    }
}

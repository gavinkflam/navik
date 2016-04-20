package hk.gavin.navik.core.map.event;

import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.map.NKMapFragment;

public class MapMarkerClickEvent {

    public final NKMapFragment map;
    public final int markerId;
    public final NKLocation location;

    public MapMarkerClickEvent(NKMapFragment map, int markerId, NKLocation location) {
        this.map = map;
        this.markerId = markerId;
        this.location = location;
    }
}

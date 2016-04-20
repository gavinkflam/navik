package hk.gavin.navik.core.map.event;

import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.map.NKMapFragment;

public class MapLongPressEvent {

    public final NKMapFragment map;
    public final NKLocation location;

    public MapLongPressEvent(NKMapFragment map, NKLocation location) {
        this.map = map;
        this.location = location;
    }
}

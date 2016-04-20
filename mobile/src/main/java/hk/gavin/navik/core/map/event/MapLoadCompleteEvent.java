package hk.gavin.navik.core.map.event;

import hk.gavin.navik.core.map.NKMapFragment;

public class MapLoadCompleteEvent {

    public final NKMapFragment map;

    public MapLoadCompleteEvent(NKMapFragment map) {
        this.map = map;
    }
}

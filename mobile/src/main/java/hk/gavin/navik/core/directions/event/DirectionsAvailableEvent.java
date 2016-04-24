package hk.gavin.navik.core.directions.event;

import hk.gavin.navik.core.directions.NKDirections;

public class DirectionsAvailableEvent {

    public final NKDirections directions;

    public DirectionsAvailableEvent(NKDirections directions) {
        this.directions = directions;
    }
}

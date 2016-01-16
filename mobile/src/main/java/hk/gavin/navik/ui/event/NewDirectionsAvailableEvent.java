package hk.gavin.navik.ui.event;

import hk.gavin.navik.core.directions.NKDirections;

public class NewDirectionsAvailableEvent {

    public final NKDirections directions;

    public NewDirectionsAvailableEvent(NKDirections directions) {
        this.directions = directions;
    }
}

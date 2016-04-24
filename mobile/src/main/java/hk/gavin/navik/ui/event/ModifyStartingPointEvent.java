package hk.gavin.navik.ui.event;

import hk.gavin.navik.core.location.NKLocation;

public class ModifyStartingPointEvent {

    public final NKLocation location;

    public ModifyStartingPointEvent(NKLocation location) {
        this.location = location;
    }
}

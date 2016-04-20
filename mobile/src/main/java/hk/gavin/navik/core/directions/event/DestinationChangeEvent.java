package hk.gavin.navik.core.directions.event;

import com.google.common.base.Optional;
import hk.gavin.navik.core.location.NKLocation;

public class DestinationChangeEvent {

    public final Optional<NKLocation> location;

    public DestinationChangeEvent(Optional<NKLocation> location) {
        this.location = location;
    }
}

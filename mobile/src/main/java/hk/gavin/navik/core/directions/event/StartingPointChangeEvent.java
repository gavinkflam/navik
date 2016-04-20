package hk.gavin.navik.core.directions.event;

import com.google.common.base.Optional;
import hk.gavin.navik.core.location.NKLocation;

public class StartingPointChangeEvent {

    public final Optional<NKLocation> location;

    public StartingPointChangeEvent(Optional<NKLocation> location) {
        this.location = location;
    }
}

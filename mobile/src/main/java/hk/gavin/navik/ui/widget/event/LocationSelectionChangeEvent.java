package hk.gavin.navik.ui.widget.event;

import com.google.common.base.Optional;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.ui.widget.LocationSelector;

public class LocationSelectionChangeEvent {

    public final LocationSelector selector;
    public final Optional<NKLocation> location;

    public LocationSelectionChangeEvent(LocationSelector selector, Optional<NKLocation> location) {
        this.selector = selector;
        this.location = location;
    }
}

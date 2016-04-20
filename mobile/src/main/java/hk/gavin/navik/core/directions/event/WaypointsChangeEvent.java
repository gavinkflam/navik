package hk.gavin.navik.core.directions.event;

import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.location.NKLocation;

import java.util.List;

public class WaypointsChangeEvent {

    public final ImmutableList<NKLocation> locations;

    public WaypointsChangeEvent(ImmutableList<NKLocation> locations) {
        this.locations = locations;
    }

    public WaypointsChangeEvent(List<NKLocation> locations) {
        this.locations = ImmutableList.copyOf(locations);
    }
}

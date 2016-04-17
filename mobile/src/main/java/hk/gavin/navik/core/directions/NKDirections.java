package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.location.NKLocation;

public abstract class NKDirections {

    public final NKLocation startingPoint;
    public final NKLocation destination;
    public final Optional<ImmutableList<NKLocation>> viaPoints;
    public final ImmutableList<NKLocation> locations;
    public final int distance;

    public NKDirections(NKLocation startingPoint, NKLocation destination,
                        Optional<ImmutableList<NKLocation>> viaPoints,
                        ImmutableList<NKLocation> locations, int distance) {
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.viaPoints = viaPoints;
        this.locations = locations;
        this.distance = distance;
    }

    public NKDirections(NKLocation startingPoint, NKLocation destination, ImmutableList<NKLocation> locations,
                        int distance) {
        this(startingPoint, destination, Optional.<ImmutableList<NKLocation>>absent(), locations, distance);
    }
}

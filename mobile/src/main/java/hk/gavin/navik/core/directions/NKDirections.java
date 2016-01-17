package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.location.NKLocation;

public abstract class NKDirections {

    public final NKLocation startingPoint;
    public final NKLocation destination;
    public final Optional<ImmutableList<NKLocation>> viaPoints;

    public NKDirections(NKLocation startingPoint, NKLocation destination,
                        Optional<ImmutableList<NKLocation>> viaPoints) {
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.viaPoints = viaPoints;
    }

    public NKDirections(NKLocation startingPoint, NKLocation destination) {
        this(startingPoint, destination, Optional.<ImmutableList<NKLocation>>absent());
    }
}

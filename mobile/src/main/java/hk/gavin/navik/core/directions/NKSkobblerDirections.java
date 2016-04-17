package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.location.NKLocation;

public class NKSkobblerDirections extends NKDirections {

    public final int cacheId;

    public NKSkobblerDirections(int cacheId, NKLocation startingPoint, NKLocation destination,
                                Optional<ImmutableList<NKLocation>> viaPoints,
                                ImmutableList<NKLocation> locations, int distance) {
        super(startingPoint, destination, viaPoints, locations, distance);
        this.cacheId = cacheId;
    }

    public NKSkobblerDirections(int cacheId, NKLocation startingPoint, NKLocation destination,
                                ImmutableList<NKLocation> locations, int distance) {
        this(cacheId, startingPoint, destination, Optional.<ImmutableList<NKLocation>>absent(), locations, distance);
    }
}

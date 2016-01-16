package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.directions.promise.NKDirectionsPromise;
import hk.gavin.navik.core.location.NKLocation;

public interface NKDirectionsProvider {

    NKDirectionsPromise getCyclingDirections(
            int noOfDirections, NKLocation startingPoint, NKLocation destination,
            Optional<ImmutableList<NKLocation>> viaPoints);
}

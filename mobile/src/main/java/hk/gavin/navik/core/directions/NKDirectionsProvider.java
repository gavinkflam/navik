package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.directions.promise.NKDirectionsPromise;
import hk.gavin.navik.core.location.NKLocation;

public interface NKDirectionsProvider {

    NKDirectionsPromise getCyclingDirections(
            boolean offline, int noOfDirections, NKLocation startingPoint, NKLocation destination,
            Optional<ImmutableList<NKLocation>> viaPoints);
    NKDirectionsPromise getCyclingDirectionsFromGpxFile(String gpxPath);
}

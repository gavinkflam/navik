package hk.gavin.navik.core.directions.event;

import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.contract.DirectionsType;

public class DirectionsAvailableEvent {

    public final ImmutableList<NKDirections> directionsList;
    public final DirectionsType directionsType;

    public DirectionsAvailableEvent(ImmutableList<NKDirections> directionsList, DirectionsType directionsType) {
        this.directionsList = directionsList;
        this.directionsType = directionsType;
    }
}

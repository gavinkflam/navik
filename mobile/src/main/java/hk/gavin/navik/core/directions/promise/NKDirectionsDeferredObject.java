package hk.gavin.navik.core.directions.promise;

import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import org.jdeferred.impl.DeferredObject;

public class NKDirectionsDeferredObject extends
        DeferredObject<ImmutableList<NKDirections>, NKDirectionsException, Void> implements NKDirectionsPromise {

    public NKDirectionsPromise promise() {
        return this;
    }
}

package hk.gavin.navik.core.directions.promise;

import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import org.jdeferred.Promise;

public interface NKDirectionsPromise extends
        Promise<ImmutableList<NKDirections>, NKDirectionsException, Void> {

}

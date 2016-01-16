package hk.gavin.navik.core.directions.exception;

import com.skobbler.ngx.routing.SKRouteListener;

public class NKNoRoutesAvailableException extends NKDirectionsException {

    public NKNoRoutesAvailableException(SKRouteListener.SKRoutingErrorCode errorCode) {
        super(errorCode.toString());
    }
}

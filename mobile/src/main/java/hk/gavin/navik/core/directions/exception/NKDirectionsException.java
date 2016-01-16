package hk.gavin.navik.core.directions.exception;

public abstract class NKDirectionsException extends Exception {

    public NKDirectionsException() {
        super();
    }

    public NKDirectionsException(String error) {
        super(error);
    }
}

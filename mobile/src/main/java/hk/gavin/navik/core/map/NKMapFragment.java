package hk.gavin.navik.core.map;

import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.ui.fragment.AbstractUiFragment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class NKMapFragment extends AbstractUiFragment {

    @Getter @Setter(AccessLevel.PROTECTED) private boolean mMapLoaded = false;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private boolean mPendingMoveToCurrentLocation = false;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private boolean mDisplayMoveToCurrentLocationButton = false;

    abstract public NKLocation getMapCenter();

    abstract public void moveToCurrentLocation();
    abstract public void moveToCurrentLocationOnceAvailable();

    abstract public void showMoveToCurrentLocationButton();
    abstract public void hideMoveToCurrentLocationButton();

    abstract public void showRoute(NKDirections directions, boolean zoom);
    abstract public void zoomToCurrentRoute();
    abstract public void clearCurrentRoute();

    abstract public void addMarker(int id, NKLocation location, MarkerIcon icon);
    abstract public void removeMarker(int id);
    abstract public void clearMarkers();

    public enum MarkerIcon {
        Red, Green, Blue, Flag
    }
}

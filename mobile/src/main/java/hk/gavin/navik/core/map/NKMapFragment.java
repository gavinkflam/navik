package hk.gavin.navik.core.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.google.common.base.Optional;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.location.NKLocation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class NKMapFragment extends Fragment {

    @Getter(AccessLevel.PROTECTED) private Optional<MapEventsListener> mMapEventsListener = Optional.absent();

    @Getter @Setter(AccessLevel.PROTECTED) private boolean mMapLoaded = false;
    @Getter(AccessLevel.PROTECTED) private boolean mViewInjected = false;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private boolean mPendingMoveToCurrentLocation = false;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private boolean mDisplayMoveToCurrentLocationButton = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mViewInjected = true;
    }

    public void setMapEventsListener(MapEventsListener listener) {
        mMapEventsListener = Optional.of(listener);
    }

    abstract public NKLocation getMapCenter();

    abstract public void moveToCurrentLocation();
    abstract public void moveToCurrentLocationOnceAvailable();

    abstract public void showMoveToCurrentLocationButton();
    abstract public void hideMoveToCurrentLocationButton();

    abstract public void showRoute(NKDirections directions, boolean zoom);
    abstract public void zoomToCurrentRoute();
    abstract public void clearCurrentRoute();

    abstract public void startNavigation();
    abstract public void stopNavigation();

    abstract protected int getLayoutResId();

    public interface MapEventsListener {
        void onMapLoadComplete();
    }
}

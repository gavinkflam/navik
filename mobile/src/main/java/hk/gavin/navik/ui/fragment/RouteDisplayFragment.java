package hk.gavin.navik.ui.fragment;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class RouteDisplayFragment extends AbstractHomeUiFragment implements
        NKInteractiveDirectionsProvider.DirectionsResultsListener {

    @Inject NKLocationProvider mLocationProvider;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    private NKMapFragment mMap;
    private Optional<NKDirections> mDirections = Optional.absent();

    @Getter private final int mLayoutResId = R.layout.fragment_route_display;

    public void clearRouteDisplay() {
        if (isActivityCreated()) {
            mMap.clearCurrentRoute();
        }
    }

    @Override
    public void onInitialize() {
        if (isActivityCreated() && !isInitialized()) {
            mMap = getController().getMap();
            mMap.moveToCurrentLocationOnceAvailable();
            super.onInitialize();

            onViewVisible();
        }
    }

    @Override
    public void onViewVisible() {
        if (isActivityCreated() && isInitialized()) {
            mMap.hideMoveToCurrentLocationButton();

            if (mMap.isMapLoaded() && mDirections.isPresent()) {
                mMap.showRoute(mDirections.get(), true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isActivityCreated()) {
            mDirectionsProvider.addDirectionsResultsListener(this);
        }
    }

    @Override
    public void onPause() {
        if (isActivityCreated()) {
            mDirectionsProvider.removeDirectionsResultsListener(this);
        }
        super.onPause();
    }

    @Override
    public void onDirectionsAvailable(ImmutableList<NKDirections> directionsList, boolean isManualUpdate) {
        mDirections = Optional.of(directionsList.get(0));
        if (mMap.isMapLoaded()) {
            mMap.showRoute(mDirections.get(), isManualUpdate);
        }
    }

    @Override
    public void onDirectionsError(NKDirectionsException exception, boolean isManualUpdate) {
        // Do nothing
    }
}

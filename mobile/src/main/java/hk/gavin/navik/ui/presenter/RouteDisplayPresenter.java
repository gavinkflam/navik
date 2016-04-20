package hk.gavin.navik.ui.presenter;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.map.NKMapFragment;
import lombok.Getter;

import javax.inject.Inject;

public class RouteDisplayPresenter extends AbstractPresenter implements NKInteractiveDirectionsProvider.DirectionsResultsListener {

    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Getter private NKMapFragment mMap;
    private Optional<NKDirections> mDirections = Optional.absent();

    public void setMap(NKMapFragment map) {
        mMap = map;
        mMap.showMoveToCurrentLocationButton();
        mMap.moveToCurrentLocationOnceAvailable();
        invalidate();
    }

    @Override
    public void invalidate() {
        if (mMap == null || !mDirections.isPresent()) {
            return;
        }

        mMap.showRoute(mDirections.get(), true);
    }

    @Override
    public void onResume() {
        mDirectionsProvider.addDirectionsResultsListener(this);
    }

    @Override
    public void onPause() {
        mDirectionsProvider.removeDirectionsResultsListener(this);
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

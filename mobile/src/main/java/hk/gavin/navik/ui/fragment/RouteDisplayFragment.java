package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Properly set map display
        mMap = getController().getMap();
        mMap.hideMoveToCurrentLocationButton();
        mMap.moveToCurrentLocationOnceAvailable();

        if (mMap.isMapLoaded() && mDirections.isPresent()) {
            mMap.showRoute(mDirections.get(), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDirectionsProvider.addDirectionsResultsListener(this);
    }

    @Override
    public void onPause() {
        mDirectionsProvider.removeDirectionsResultsListener(this);
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

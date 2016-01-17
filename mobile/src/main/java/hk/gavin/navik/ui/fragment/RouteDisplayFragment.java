package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.controller.HomeController;

import javax.inject.Inject;

public class RouteDisplayFragment extends AbstractUiFragment implements NKInteractiveDirectionsProvider.DirectionsResultsListener {

    @Inject NKLocationProvider mLocationProvider;
    @Inject HomeController mController;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    private NKMapFragment mMap;
    private Optional<NKDirections> mDirections = Optional.absent();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).component().inject(this);

        initializeFragments();
        initializeViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_display, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeFragments();
        initializeViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        onViewVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDirectionsProvider.removeDirectionsResultsListener(this);
    }

    @Override
    public void onViewVisible() {
        if (mMap == null) {
            return;
        }

        mDirectionsProvider.addDirectionsResultsListener(this);
        mMap.hideMoveToCurrentLocationButton();
        if (mMap.isMapLoaded() && mDirections.isPresent()) {
            mMap.showRoute(mDirections.get(), true);
        }
    }

    private void initializeFragments() {
        if (mController == null) {
            return;
        }

        mMap = mController.getMap();
        mMap.moveToCurrentLocationOnceAvailable();
        onViewVisible();
    }

    private void initializeViews() {
        if (mMap == null) {
            return;
        }

        mMap.clearCurrentRoute();
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

    }
}

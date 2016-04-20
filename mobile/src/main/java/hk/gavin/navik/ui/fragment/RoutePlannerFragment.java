package hk.gavin.navik.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.OnClick;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.ui.widget.LocationSelector;
import hk.gavin.navik.ui.widget.TwoStatedFloatingActionButton;
import hk.gavin.navik.ui.widget.event.LocationSelectionChangeEvent;
import hk.gavin.navik.ui.widget.event.SelectCurrentLocationEvent;
import hk.gavin.navik.ui.widget.event.SelectLocationOnMapEvent;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class RoutePlannerFragment extends AbstractHomeUiFragment implements NKMapFragment.MapEventsListener {

    @Inject NKLocationProvider mLocationProvider;
    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.startBikeNavigation) TwoStatedFloatingActionButton mStartBikeNavigation;
    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;

    private NKMapFragment mMap;
    private Optional<NKDirections> mDirections = Optional.absent();

    @Getter private final int mLayoutResId = R.layout.fragment_route_planner;

    public RoutePlannerFragment() {
        setHasOptionsMenu(true);
    }

    @OnClick(R.id.startBikeNavigation)
    void startBikeNavigation() {
        if (mDirections.isPresent()) {
            getController().startBikeNavigation(mDirections.get());
        }
    }

    @OnClick(R.id.showRouteAnalysis)
    void showRouteAnalysis() {
        if (mDirections.isPresent()) {
            getController().showRouteAnalysis(mDirections.get());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getController().initializeRouteDisplayFragment();

        // Add as map event listener
        mMap = getController().getMap();
        mMap.setMapEventsListener(this);

        // Update title and back button display
        getController().setActionBarTitle(R.string.app_name);
        getController().setDisplayHomeAsUp(false);

        // Initialize location selector
        mStartingPoint.initialize(mReverseGeocoder);
        mDestination.initialize(mReverseGeocoder);
    }

    @Override
    public void onResume() {
        super.onResume();

        NKBus.get().register(this);
        if (mDirections.isPresent()) {
            mStartBikeNavigation.enable();
        }
        else {
            mStartBikeNavigation.disable();
        }
    }

    @Override
    public void onPause() {
        NKBus.get().unregister(this);
        super.onPause();
    }

    @Override
    public void onResultAvailable(Optional<Integer> requestCode, int resultCode, Optional<Intent> data) {
        if (requestCode.isPresent() && data.isPresent()) {
            NKLocation location = (NKLocation) data.get().getSerializableExtra(UiContract.DataKey.LOCATION);

            switch (requestCode.get()) {
                case UiContract.RequestCode.STARTING_POINT_LOCATION: {
                    if (resultCode == UiContract.ResultCode.OK) {
                        mStartingPoint.setLocation(location);
                    }
                    break;
                }
                case UiContract.RequestCode.DESTINATION_LOCATION: {
                    if (resultCode == UiContract.ResultCode.OK) {
                        mDestination.setLocation(location);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_offline_data: {
                return true;
            }
            case R.id.action_settings: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onLocationSelectionChanged(LocationSelectionChangeEvent event) {
        switch (event.selector.getId()) {
            case R.id.startingPoint: {
                if (event.location.isPresent()) {
                    mDirectionsProvider.setStartingPoint(mStartingPoint.getLocation());
                    mDirectionsProvider.getCyclingDirections();
                }
                break;
            }
            case R.id.destination: {
                if (event.location.isPresent()) {
                    mDirectionsProvider.setDestination(mDestination.getLocation());
                    mDirectionsProvider.getCyclingDirections();
                }
                break;
            }
        }
    }

    @Subscribe
    public void onSelectCurrentLocation(SelectCurrentLocationEvent event) {
        if (mLocationProvider.isLastLocationAvailable()) {
            event.selector.setLocation(mLocationProvider.getLastLocation().get());
        }
        else {
            getController().showMessage(R.string.error_location_not_available);
        }
    }

    @Subscribe
    public void onSelectLocationOnMap(SelectLocationOnMapEvent event) {
        switch (event.selector.getId()) {
            case R.id.startingPoint: {
                getController().selectStartingPoint();
                break;
            }
            case R.id.destination: {
                getController().selectDestination();
                break;
            }
        }
    }

    @Subscribe
    public void onDirectionsAvailable(ImmutableList<NKDirections> directionsList) {
        mDirections = Optional.of(directionsList.get(0));
        mStartBikeNavigation.enable();
    }

    @Subscribe
    public void onDirectionsError(NKDirectionsException exception) {
        mStartBikeNavigation.disable();
        getController().showMessage(R.string.error_route_not_available);
    }

    @Override
    public void onMapLoadComplete() {
        // Do nothing
    }

    @Override
    public void onLongPress(NKLocation location) {
        mDirectionsProvider.addWaypoints(location);
        mDirectionsProvider.getCyclingDirections();
    }

    @Override
    public void onMarkerClicked(int id, NKLocation location) {
        // Do nothing
    }
}

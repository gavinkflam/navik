package hk.gavin.navik.ui.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.OnClick;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.orhanobut.logger.Logger;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.ui.contract.UiContract;
import hk.gavin.navik.ui.widget.LocationSelector;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class RoutePlannerFragment extends AbstractHomeUiFragment implements
        LocationSelector.OnLocationUpdatedListener, LocationSelector.OnMenuItemClickListener,
        NKInteractiveDirectionsProvider.DirectionsResultsListener {

    @Inject NKLocationProvider mLocationProvider;
    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.startBikeNavigation) FloatingActionButton mStartBikeNavigation;
    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;

    private RouteDisplayFragment mRouteDisplay;
    private Optional<NKDirections> mDirections = Optional.absent();
    @Getter private final int mLayoutResId = R.layout.fragment_route_planner;

    public RoutePlannerFragment() {
        setHasOptionsMenu(true);
    }

    @OnClick(R.id.startBikeNavigation)
    void startBikeNavigation() {
        if (mDirections.isPresent()) {
            getController().startBikeNavigation();
        }
    }

    @Override
    public void onInitialize() {
        Logger.i("activityCreated: %b, initialized: %b", isActivityCreated(), isInitialized());
        if (isActivityCreated() && !isInitialized()) {
            mRouteDisplay = getController().initializeRouteDisplayFragment();
            super.onInitialize();
        }
    }

    @Override
    public void onInitializeViews() {
        Logger.i(
                "activityCreated: %b, viewInjected: %b, viewInitialized: %b",
                isActivityCreated(), isViewsInjected(), isViewsInitialized()
        );
        if (isActivityCreated() && isViewsInjected() && !isViewsInitialized()) {
            mStartingPoint.initialize(mLocationProvider, mReverseGeocoder);
            mDestination.initialize(mLocationProvider, mReverseGeocoder);

            mDirectionsProvider.removeStartingPoint();
            mDirectionsProvider.removeDestination();
            mDirectionsProvider.removeViaPoints();

            mStartingPoint.useCurrentLocation();
            mDestination.removeLocation();
            mRouteDisplay.clearRouteDisplay();

            mStartingPoint.setOnLocationUpdatedListener(this);
            mStartingPoint.setOnMenuItemClickListener(this);
            mDestination.setOnLocationUpdatedListener(this);
            mDestination.setOnMenuItemClickListener(this);

            super.onInitializeViews();
        }
    }

    @Override
    public void onViewVisible() {
        Logger.i("activityCreated: %b", isActivityCreated());
        if (isActivityCreated()) {
            getController().setActionBarTitle(R.string.app_name);
            getController().setDisplayHomeAsUp(false);

            mRouteDisplay.onViewVisible();
        }
    }

    @Override
    public void onResultAvailable(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UiContract.RequestCode.STARTING_POINT_LOCATION: {
                if (resultCode == UiContract.ResultCode.OK) {
                    NKLocation location = (NKLocation) data.getSerializableExtra(UiContract.DataKey.LOCATION);
                    mStartingPoint.setLocation(location, true);
                }
                break;
            }
            case UiContract.RequestCode.DESTINATION_LOCATION: {
                if (resultCode == UiContract.ResultCode.OK) {
                    NKLocation location = (NKLocation) data.getSerializableExtra(UiContract.DataKey.LOCATION);
                    mDestination.setLocation(location, true);
                }
                break;
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

    @Override
    public void onLocationUpdated(LocationSelector selector, NKLocation location, boolean isManualUpdate) {
        switch (selector.getId()) {
            case R.id.startingPoint: {
                if (mStartingPoint.isLocationAvailable()) {
                    mDirectionsProvider.setManualUpdate(isManualUpdate);
                    mDirectionsProvider.setStartingPoint(mStartingPoint.getLocation());
                    mDirectionsProvider.getCyclingDirections();
                }
                break;
            }
            case R.id.destination: {
                if (mDestination.isLocationAvailable()) {
                    mDirectionsProvider.setManualUpdate(isManualUpdate);
                    mDirectionsProvider.setDestination(mDestination.getLocation());
                    mDirectionsProvider.getCyclingDirections();
                }
                break;
            }
        }
    }

    @Override
    public void onCurrentLocationClicked(LocationSelector selector) {
        switch (selector.getId()) {
            case R.id.startingPoint: {
                mStartingPoint.useCurrentLocation();
                break;
            }
            case R.id.destination: {
                mDestination.useCurrentLocation();
                break;
            }
        }
    }

    @Override
    public void onHistoryClicked(LocationSelector selector) {
        // Do nothing
    }

    @Override
    public void onSelectLocationOnMapClicked(LocationSelector selector) {
        switch (selector.getId()) {
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

    @Override
    public void onDirectionsAvailable(ImmutableList<NKDirections> directionsList, boolean isManualUpdate) {
        mDirections = Optional.of(directionsList.get(0));
    }

    @Override
    public void onDirectionsError(NKDirectionsException exception, boolean isManualUpdate) {
        // Do nothing
    }
}

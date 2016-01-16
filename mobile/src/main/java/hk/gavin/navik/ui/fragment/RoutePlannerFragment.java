package hk.gavin.navik.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.view.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.contract.UiContract;
import hk.gavin.navik.ui.controller.HomeController;
import hk.gavin.navik.ui.widget.LocationSelector;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class RoutePlannerFragment extends AbstractUiFragment implements
        LocationSelector.OnLocationUpdatedListener, LocationSelector.OnMenuItemClickListener {

    @Inject HomeController mController;
    @Inject NKLocationProvider mLocationProvider;
    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.startBikeNavigation) FloatingActionButton mStartBikeNavigation;
    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;

    private RouteDisplayFragment mRouteDisplay;

    public RoutePlannerFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).component().inject(this);

        initializeFragments();
        initializeViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_planner, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initializeFragments();
        initializeViews();
    }

    @Override
    public void onViewVisible() {
        if (mRouteDisplay != null) {
            mRouteDisplay.onViewVisible();
        }

        ActionBar actionBar = mController.getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onResultAvailable(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UiContract.RequestCode.STARTING_POINT_LOCATION: {
                if (resultCode == UiContract.ResultCode.OK) {
                    NKLocation location = (NKLocation) data.getSerializableExtra(UiContract.DataKey.LOCATION);
                    mStartingPoint.setLocation(location);
                }
                break;
            }
            case UiContract.RequestCode.DESTINATION_LOCATION: {
                if (resultCode == UiContract.ResultCode.OK) {
                    NKLocation location = (NKLocation) data.getSerializableExtra(UiContract.DataKey.LOCATION);
                    mDestination.setLocation(location);
                }
                break;
            }
        }
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

    @OnClick(R.id.startBikeNavigation)
    void startBikeNavigation() {
        mController.startBikeNavigation();
    }

    private void initializeFragments() {
        if (mController == null) {
            return;
        }

        mRouteDisplay = mController.replaceFragment(
                R.id.homeContentFrame, RouteDisplayFragment.class, UiContract.FragmentTag.ROUTE_DISPLAY
        );
    }

    private void initializeViews() {
        if (mLocationProvider == null || mStartingPoint == null) {
            return;
        }

        mStartingPoint.initialize(mLocationProvider, mReverseGeocoder);
        mDestination.initialize(mLocationProvider, mReverseGeocoder);

        mDirectionsProvider.removeStartingPoint();
        mDirectionsProvider.removeDestination();
        mDirectionsProvider.removeViaPoints();
        
        mStartingPoint.useCurrentLocation();
        mDestination.setLocation(null);

        mStartingPoint.setOnLocationUpdatedListener(this);
        mStartingPoint.setOnMenuItemClickListener(this);
        mDestination.setOnLocationUpdatedListener(this);
        mDestination.setOnMenuItemClickListener(this);
    }

    @Override
    public void onLocationUpdated(LocationSelector selector, NKLocation location) {
        switch (selector.getId()) {
            case R.id.startingPoint: {
                if (mStartingPoint.isLocationAvailable()) {
                    mDirectionsProvider.setStartingPoint(mStartingPoint.getLocation());
                    mDirectionsProvider.getCyclingDirections();
                }
                break;
            }
            case R.id.destination: {
                if (mDestination.isLocationAvailable()) {
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

    }

    @Override
    public void onSelectLocationOnMapClicked(LocationSelector selector) {
        switch (selector.getId()) {
            case R.id.startingPoint: {
                mController.selectStartingPoint();
                break;
            }
            case R.id.destination: {
                mController.selectDestination();
                break;
            }
        }
    }
}

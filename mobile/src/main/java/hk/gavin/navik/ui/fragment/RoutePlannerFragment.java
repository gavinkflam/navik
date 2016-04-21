package hk.gavin.navik.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import butterknife.Bind;
import butterknife.OnClick;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.orhanobut.logger.Logger;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.contract.DirectionsType;
import hk.gavin.navik.core.directions.event.DirectionsAvailableEvent;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.event.MapLongPressEvent;
import hk.gavin.navik.core.map.event.MapMarkerClickEvent;
import hk.gavin.navik.preference.MainPreferences;
import hk.gavin.navik.ui.widget.LocationSelector;
import hk.gavin.navik.ui.widget.TwoStatedFloatingActionButton;
import hk.gavin.navik.ui.widget.event.LocationSelectionChangeEvent;
import hk.gavin.navik.ui.widget.event.SelectAsStartingPointEvent;
import hk.gavin.navik.ui.widget.event.SelectCurrentLocationEvent;
import hk.gavin.navik.ui.widget.event.SelectLocationOnMapEvent;
import hk.gavin.navik.util.FilePickerUtility;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class RoutePlannerFragment extends AbstractHomeUiFragment implements PopupMenu.OnMenuItemClickListener {

    @Inject MainPreferences mMainPreferences;
    @Inject NKLocationProvider mLocationProvider;
    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.startBikeNavigation) TwoStatedFloatingActionButton mStartBikeNavigation;
    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;
    @Bind(R.id.route_planner_center) View mRoutePlannerCenter;

    private Optional<NKDirections> mDirections = Optional.absent();

    private PopupMenu mMapLongPressMenu;
    private PopupMenu mWaypointkMenu;
    private Optional<MapLongPressEvent> mLastMapLongPressEvent = Optional.absent();
    private Optional<MapMarkerClickEvent> mLastMapMarkerClickEvent = Optional.absent();

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
        preparePopupMenus();

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
        mMainPreferences.setLastLocation(getController().getMap().getMapCenter());
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

    private void preparePopupMenus() {
        mMapLongPressMenu = new PopupMenu(getActivity(), mRoutePlannerCenter);
        mWaypointkMenu = new PopupMenu(getActivity(), mRoutePlannerCenter);

        mMapLongPressMenu.inflate(R.menu.popup_menu_map_long_click);
        mWaypointkMenu.inflate(R.menu.popup_menu_waypoint);

        mMapLongPressMenu.setOnMenuItemClickListener(this);
        mWaypointkMenu.setOnMenuItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_gpx_file: {
                startActivityForResult(
                        FilePickerUtility.pickGpxFileIntent(getActivity()),
                        UiContract.RequestCode.SELECT_GPX_FILE
                );
                return true;
            }
            case R.id.action_settings: {
                getController().showAppSetting();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UiContract.RequestCode.SELECT_GPX_FILE: {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    Logger.d("uri: %s, path: %s", uri, path);

                    if (path.endsWith(".gpx")) {
                        mDirectionsProvider.getCyclingDirectionsFromGpxFile(path);
                    }
                    else {
                        getController().showMessage(R.string.error_not_gpx_file);
                    }
                }
                break;
            }
        }
    }

    @Subscribe
    public void onLocationSelectionChanged(LocationSelectionChangeEvent event) {
        switch (event.selector.getId()) {
            case R.id.startingPoint: {
                if (mStartingPoint.getLocation().isPresent()) {
                    mDirectionsProvider.setStartingPoint(mStartingPoint.getLocation().get());
                    mDirectionsProvider.getCyclingDirections();
                }
                break;
            }
            case R.id.destination: {
                if (mDestination.getLocation().isPresent()) {
                    mDirectionsProvider.setDestination(mDestination.getLocation().get());
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
    public void onSelectStartingPointAsDestination(SelectAsStartingPointEvent event) {
        if (mStartingPoint.getLocation().isPresent()) {
            mDestination.setLocation(mStartingPoint.getLocation().get());
        }
        else {
            getController().showMessage(R.string.error_select_starting_point_first);
        }
    }

    @Subscribe
    public void onDirectionsAvailable(DirectionsAvailableEvent event) {
        mDirections = Optional.of(event.directionsList.get(0));
        mStartBikeNavigation.enable();

        if (event.directionsType == DirectionsType.ExternalFile) {
            mStartingPoint.setLocation(mDirections.get().startingPoint, true);
            mDestination.setLocation(mDirections.get().destination, true);
        }
    }

    @Subscribe
    public void onDirectionsError(NKDirectionsException exception) {
        mStartBikeNavigation.disable();
        getController().showMessage(R.string.error_route_not_available);
    }

    @Subscribe
    public void onLongPress(MapLongPressEvent event) {
        mLastMapLongPressEvent = Optional.of(event);
        mMapLongPressMenu.show();
    }

    @Subscribe
    public void onMarkerClicked(MapMarkerClickEvent event) {
        if (event.markerId > 1) {
            mLastMapMarkerClickEvent = Optional.of(event);
            mWaypointkMenu.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_as_starting_point:
                mStartingPoint.setLocation(mLastMapLongPressEvent.get().location);
                return true;
            case R.id.set_as_destination:
                mDestination.setLocation(mLastMapLongPressEvent.get().location);
                return true;
            case R.id.add_as_waypoint:
                mDirectionsProvider.addWaypoints(mLastMapLongPressEvent.get().location);
                mDirectionsProvider.getCyclingDirections();
                return true;
            case R.id.remove_waypoint:
                mDirectionsProvider.removeWaypoint(mLastMapMarkerClickEvent.get().markerId - 2);
                mDirectionsProvider.getCyclingDirections();
                return true;
        }

        return false;
    }
}

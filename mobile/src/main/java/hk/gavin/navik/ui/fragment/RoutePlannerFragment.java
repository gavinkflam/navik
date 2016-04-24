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
import hk.gavin.navik.core.directions.event.*;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.event.MapLongPressEvent;
import hk.gavin.navik.core.map.event.MapMarkerClickEvent;
import hk.gavin.navik.preference.MainPreferences;
import hk.gavin.navik.ui.activity.NavigationActivity;
import hk.gavin.navik.ui.presenter.RoutePlannerPresenter;
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
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.route_planner_center) View mRoutePlannerCenter;

    private RoutePlannerPresenter mPresenter = new RoutePlannerPresenter();
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
            Intent data = new Intent(getActivity(), NavigationActivity.class);
            data.putExtra(UiContract.DataKey.DIRECTIONS, mDirections.get());
            startActivityForResult(data, UiContract.RequestCode.NAVIGATION);
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

        // Initialize presenter
        component().inject(mPresenter);
        mPresenter.onDependencyResolved();

        // Update title and back button display
        getController().setActionBarTitle(R.string.app_name);
        getController().setDisplayHomeAsUp(false);

        preparePopupMenus();
    }

    @Override
    public void onResume() {
        super.onResume();
        NKBus.get().register(this);
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        mMainPreferences.setLastLocation(getController().getMap().getMapCenter());
        NKBus.get().unregister(this);
        mPresenter.onPause();
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onViewCreated(view, savedInstanceState);
        mPresenter.invalidate();
    }

    @Override
    public void onResultAvailable(Optional<Integer> requestCode, int resultCode, Optional<Intent> data) {
        if (requestCode.isPresent() && data.isPresent()) {
            NKLocation location = (NKLocation) data.get().getSerializableExtra(UiContract.DataKey.LOCATION);

            switch (requestCode.get()) {
                case UiContract.RequestCode.STARTING_POINT_LOCATION: {
                    if (resultCode == UiContract.ResultCode.OK) {
                        mDirectionsProvider.setStartingPoint(location);
                    }
                    break;
                }
                case UiContract.RequestCode.DESTINATION_LOCATION: {
                    if (resultCode == UiContract.ResultCode.OK) {
                        mDirectionsProvider.setDestination(location);
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
            case UiContract.RequestCode.NAVIGATION: {
                Logger.d("onActivityResult: UiContract.RequestCode.NAVIGATION");

                // Clear route and location selection
                mDirections = Optional.absent();
                mDirectionsProvider.removeStartingPoint();
                mDirectionsProvider.removeDestination();
                mDirectionsProvider.clearWaypoints();

                // Post event
                NKBus.get().post(new NavigationActivity());
                break;
            }
        }
    }

    @Subscribe
    public void onSelectCurrentLocation(SelectCurrentLocationEvent event) {
        if (mLocationProvider.isLastLocationAvailable()) {
            NKLocation location = mLocationProvider.getLastLocation().get();

            switch (event.selector.getId()) {
                case R.id.startingPoint: {
                    mDirectionsProvider.setStartingPoint(location);
                    break;
                }
                case R.id.destination: {
                    mDirectionsProvider.setDestination(location);
                    break;
                }
            }
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
        if (mDirectionsProvider.getStartingPoint().isPresent()) {
            mDirectionsProvider.setDestination(mDirectionsProvider.getStartingPoint().get());
        }
        else {
            getController().showMessage(R.string.error_select_starting_point_first);
        }
    }

    @Subscribe
    public void onStartingPointChanged(StartingPointChangeEvent event) {
        mDirectionsProvider.getCyclingDirections();
    }

    @Subscribe
    public void onDestinationChanged(DestinationChangeEvent event) {
        mDirectionsProvider.getCyclingDirections();
    }

    @Subscribe
    public void onWaypointsChanged(WaypointsChangeEvent event) {
        mDirectionsProvider.getCyclingDirections();
    }

    @Subscribe
    public void onRoutingInProgress(RoutingInProgressEvent event) {
        getController().showMessage(R.string.routing_in_progress);
    }

    @Subscribe
    public void onDirectionsAvailable(DirectionsAvailableEvent event) {
        getController().showMessage(R.string.routing_completed);
        mDirections = Optional.of(event.directionsList.get(0));
    }

    @Subscribe
    public void onDirectionsError(NKDirectionsException exception) {
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
                mDirectionsProvider.setStartingPoint(mLastMapLongPressEvent.get().location);
                return true;
            case R.id.set_as_destination:
                mDirectionsProvider.setDestination(mLastMapLongPressEvent.get().location);
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

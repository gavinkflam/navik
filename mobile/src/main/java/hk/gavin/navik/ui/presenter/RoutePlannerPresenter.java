package hk.gavin.navik.ui.presenter;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import butterknife.Bind;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.contract.DirectionsType;
import hk.gavin.navik.core.directions.event.DestinationChangeEvent;
import hk.gavin.navik.core.directions.event.DirectionsAvailableEvent;
import hk.gavin.navik.core.directions.event.RoutingInProgressEvent;
import hk.gavin.navik.core.directions.event.StartingPointChangeEvent;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.map.event.MapLongPressEvent;
import hk.gavin.navik.core.map.event.MapMarkerClickEvent;
import hk.gavin.navik.ui.widget.LocationSelector;
import hk.gavin.navik.ui.widget.TwoStatedFloatingActionButton;

import javax.inject.Inject;

public class RoutePlannerPresenter extends AbstractPresenter implements PopupMenu.OnMenuItemClickListener {

    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;
    @Bind(R.id.route_planner_center) View mRoutePlannerCenter;

    @Bind(R.id.startBikeNavigation) TwoStatedFloatingActionButton mStartBikeNavigation;
    @Bind(R.id.showRouteAnalysis) FloatingActionButton mShowRouteAnalysis;

    private Optional<NKDirections> mDirections = Optional.absent();
    private boolean mRoutingInProgress = false;

    private PopupMenu mMapLongPressMenu;
    private PopupMenu mWaypointkMenu;
    private Optional<MapLongPressEvent> mLastMapLongPressEvent = Optional.absent();
    private Optional<MapMarkerClickEvent> mLastMapMarkerClickEvent = Optional.absent();

    public void invalidate() {
        if (mDirections.isPresent() && !mRoutingInProgress) {
            mStartBikeNavigation.enable();
            mShowRouteAnalysis.setEnabled(true);
        }
        else {
            mStartBikeNavigation.disable();
            mShowRouteAnalysis.setEnabled(false);
        }
    }

    private void preparePopupMenus(Activity activity) {
        mMapLongPressMenu = new PopupMenu(activity, mRoutePlannerCenter);
        mWaypointkMenu = new PopupMenu(activity, mRoutePlannerCenter);

        mMapLongPressMenu.inflate(R.menu.popup_menu_map_long_click);
        mWaypointkMenu.inflate(R.menu.popup_menu_waypoint);

        mMapLongPressMenu.setOnMenuItemClickListener(this);
        mWaypointkMenu.setOnMenuItemClickListener(this);
    }

    @Override
    public void onActivityCreated(Activity activity) {
        preparePopupMenus(activity);
    }

    @Override
    public void onResume() {
        NKBus.get().register(this);
    }

    @Override
    public void onPause() {
        NKBus.get().unregister(this);
    }

    public void onDependencyResolved() {
        // Initialize location selector
        mStartingPoint.initialize(mReverseGeocoder);
        mDestination.initialize(mReverseGeocoder);
    }

    @Subscribe
    public void onStartingPointChanged(StartingPointChangeEvent event) {
        if (event.location.isPresent()) {
            mStartingPoint.setLocation(event.location.get());
        }
        else {
            mStartingPoint.removeLocation();
        }
    }

    @Subscribe
    public void onDestinationChanged(DestinationChangeEvent event) {
        if (event.location.isPresent()) {
            mDestination.setLocation(event.location.get());
        }
        else {
            mDestination.removeLocation();
        }
    }

    @Subscribe
    public void onRoutingInProgress(RoutingInProgressEvent event) {
        mRoutingInProgress = true;
        invalidate();
    }

    @Subscribe
    public void onDirectionsAvailable(DirectionsAvailableEvent event) {
        mDirections = Optional.of(event.directions);
        mRoutingInProgress = false;
        invalidate();

        if (event.directions.getDirectionsType().equals(DirectionsType.ExternalFile)) {
            mStartingPoint.setLocation(mDirections.get().startingPoint);
            mDestination.setLocation(mDirections.get().destination);
        }
    }

    @Subscribe
    public void onDirectionsError(NKDirectionsException exception) {
        mDirections = Optional.absent();
        mRoutingInProgress = false;
        invalidate();
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
                return true;
            case R.id.remove_waypoint:
                mDirectionsProvider.removeWaypoint(mLastMapMarkerClickEvent.get().markerId - 2);
                return true;
        }

        return false;
    }
}

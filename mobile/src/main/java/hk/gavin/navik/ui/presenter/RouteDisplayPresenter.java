package hk.gavin.navik.ui.presenter;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.event.DestinationChangeEvent;
import hk.gavin.navik.core.directions.event.DirectionsAvailableEvent;
import hk.gavin.navik.core.directions.event.StartingPointChangeEvent;
import hk.gavin.navik.core.directions.event.WaypointsChangeEvent;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.ui.event.NavigationCompleteEvent;
import lombok.Getter;

import javax.inject.Inject;

public class RouteDisplayPresenter extends AbstractPresenter {

    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Getter private NKMapFragment mMap;
    private Optional<NKDirections> mDirections = Optional.absent();

    public void setMap(NKMapFragment map) {
        mMap = map;
        mMap.showMoveToCurrentLocationButton();
        mMap.moveToCurrentLocationOnceAvailable();
    }

    @Override
    public void onResume() {
        NKBus.get().register(this);
    }

    @Override
    public void onPause() {
        NKBus.get().unregister(this);
    }

    @Subscribe
    public void onStartingPointChanged(StartingPointChangeEvent event) {
        updateStartingPointMarker();
    }

    @Subscribe
    public void onDestinationChanged(DestinationChangeEvent event) {
        updateDestinationMarker();
    }

    @Subscribe
    public void onWaypointsChanged(WaypointsChangeEvent event) {
        updateWaypointsMarker();
    }

    @Subscribe
    public void onDirectionsAvailable(DirectionsAvailableEvent event) {
        mDirections = Optional.of(event.directionsList.get(0));
        if (mMap.isMapLoaded()) {
            mMap.showRoute(mDirections.get(), true);
        }
    }

    @Subscribe
    public void onDirectionsError(NKDirectionsException exception) {
        if (mMap.isMapLoaded()) {
            mMap.clearCurrentRoute();
        }
    }

    @Subscribe
    public void onNavigationCompleted(NavigationCompleteEvent event) {
        if (mMap.isMapLoaded()) {
            mMap.clearMarkers();
        }
    }

    private void updateStartingPointMarker() {
        if (mDirectionsProvider.getStartingPoint().isPresent()) {
            mMap.addMarker(0, mDirectionsProvider.getStartingPoint().get(), NKMapFragment.MarkerIcon.Green);
        }
        else {
            mMap.removeMarker(0);
        }
    }

    private void updateDestinationMarker() {
        if (mDirectionsProvider.getDestination().isPresent()) {
            mMap.addMarker(1, mDirectionsProvider.getDestination().get(), NKMapFragment.MarkerIcon.Flag);
        }
        else {
            mMap.removeMarker(1);
        }
    }

    private void updateWaypointsMarker() {
        ImmutableList<NKLocation> waypoints = mDirectionsProvider.getWaypoints();
        mMap.clearMarkers();

        updateStartingPointMarker();
        updateDestinationMarker();

        for (int i = 0; i < waypoints.size(); i++) {
            mMap.addMarker(i + 2, waypoints.get(i), NKMapFragment.MarkerIcon.Blue);
        }
    }
}

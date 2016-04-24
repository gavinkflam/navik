package hk.gavin.navik.ui.presenter;

import android.support.design.widget.FloatingActionButton;
import butterknife.Bind;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.contract.DirectionsType;
import hk.gavin.navik.core.directions.event.*;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.ui.widget.LocationSelector;
import hk.gavin.navik.ui.widget.TwoStatedFloatingActionButton;

import javax.inject.Inject;

public class RoutePlannerPresenter extends AbstractPresenter {

    @Inject NKReverseGeocoder mReverseGeocoder;

    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;

    @Bind(R.id.startBikeNavigation) TwoStatedFloatingActionButton mStartBikeNavigation;
    @Bind(R.id.showRouteAnalysis) FloatingActionButton mShowRouteAnalysis;

    private Optional<NKDirections> mDirections = Optional.absent();
    private boolean mRoutingInProgress = false;

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
        mDirections = Optional.of(event.directionsList.get(0));
        mRoutingInProgress = false;
        invalidate();

        if (event.directionsType == DirectionsType.ExternalFile) {
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

}

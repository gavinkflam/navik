package hk.gavin.navik.ui.presenter;

import android.support.design.widget.FloatingActionButton;
import butterknife.Bind;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.event.DirectionsAvailableEvent;
import hk.gavin.navik.core.directions.event.RoutingInProgressEvent;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.ui.widget.TwoStatedFloatingActionButton;

public class RoutePlannerPresenter extends AbstractPresenter {

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
    }

    @Subscribe
    public void onDirectionsError(NKDirectionsException exception) {
        mDirections = Optional.absent();
        mRoutingInProgress = false;
        invalidate();
    }

}

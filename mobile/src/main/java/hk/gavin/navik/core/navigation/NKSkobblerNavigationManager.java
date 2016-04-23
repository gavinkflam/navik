package hk.gavin.navik.core.navigation;

import android.app.Activity;
import com.google.common.eventbus.Subscribe;
import com.skobbler.ngx.map.SKMapViewHolder;
import com.skobbler.ngx.navigation.SKNavigationSettings;
import com.skobbler.ngx.navigation.SKNavigationState;
import com.skobbler.ngx.routing.SKRouteManager;
import com.skobbler.ngx.routing.SKRouteSettings;
import com.skobbler.ngx.sdktools.navigationui.*;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKSkobblerDirections;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;
import hk.gavin.navik.core.navigation.event.NavigationEndedEvent;
import hk.gavin.navik.core.navigation.event.NavigationStartedEvent;
import hk.gavin.navik.core.navigation.event.NavigationStateUpdateEvent;

public class NKSkobblerNavigationManager extends NKNavigationManager implements SKToolsNavigationListener {

    private SKToolsNavigationManager mNKSKToolsNavigationManager;
    private SKMapViewHolder mSKMapViewHolder;
    private SKRouteManager mSKRouteManager = SKRouteManager.getInstance();

    public NKSkobblerNavigationManager(Activity activity, int containerId, NKMapFragment nkMapFragment) {
        super(activity, containerId, nkMapFragment);

        mNKSKToolsNavigationManager = new SKToolsNavigationManager(activity, R.id.nkSKMapContainer);
        mNKSKToolsNavigationManager.setNavigationListener(this);
        mSKMapViewHolder = ((NKSkobblerMapFragment) nkMapFragment).getMapHolder();
    }

    @Override
    public void startNavigation(NKDirections directions) {
        if (!(directions instanceof NKSkobblerDirections)) {
            return;
        }
        NKBus.get().register(this);

        mSKRouteManager.loadRouteFromCache(((NKSkobblerDirections) directions).cacheId);
        SKToolsNavigationConfiguration configuration = new SKToolsNavigationConfiguration();
        configuration.setRouteType(SKRouteSettings.SKRouteMode.BICYCLE_QUIETEST);

        if (isSimulation()) {
            configuration.setNavigationType(SKNavigationSettings.SKNavigationType.SIMULATION);
        }
        else {
            configuration.setNavigationType(SKNavigationSettings.SKNavigationType.REAL);
        }

        mNKSKToolsNavigationManager.startNavigation(configuration, mSKMapViewHolder);
    }

    @Override
    public void stopNavigation() {
        mNKSKToolsNavigationManager.stopNavigation();
        NKBus.get().unregister(this);
    }

    @Override
    public void onNavigationStarted() {
        NKBus.get().post(new NavigationStartedEvent());
    }

    @Override
    public void onNavigationEnded() {
        NKBus.get().post(new NavigationEndedEvent());
    }

    @Override
    public void onRouteCalculationStarted() {
        // Do nothing
    }

    @Override
    public void onRouteCalculationCompleted() {
        // Do nothing
    }

    @Override
    public void onRouteCalculationCanceled() {
        // Do nothing
    }

    @Subscribe
    public void onSKNavigationStateUpdate(SKNavigationState skNavigationState) {
        NKBus.get().post(new NavigationStateUpdateEvent(
                NKSkobblerNavigationUtil.createNavigationState(skNavigationState)
        ));
    }
}

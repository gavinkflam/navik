package hk.gavin.navik.core.navigation;

import android.app.Activity;
import com.skobbler.ngx.map.SKMapViewHolder;
import com.skobbler.ngx.navigation.SKNavigationSettings;
import com.skobbler.ngx.navigation.SKNavigationState;
import com.skobbler.ngx.routing.SKRouteSettings;
import com.skobbler.ngx.sdktools.navigationui.NKSKNavigationStateListener;
import com.skobbler.ngx.sdktools.navigationui.NKSKToolsNavigationManager;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationConfiguration;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationListener;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;
import hk.gavin.navik.core.navigation.event.NavigationEndedEvent;
import hk.gavin.navik.core.navigation.event.NavigationStartedEvent;
import hk.gavin.navik.core.navigation.event.NavigationStateUpdateEvent;

public class NKSkobblerNavigationManager extends NKNavigationManager implements
        SKToolsNavigationListener, NKSKNavigationStateListener {

    private NKSKToolsNavigationManager mNKSKToolsNavigationManager;
    private SKMapViewHolder mSKMapViewHolder;

    public NKSkobblerNavigationManager(Activity activity, int containerId, NKMapFragment nkMapFragment) {
        super(activity, containerId, nkMapFragment);

        mNKSKToolsNavigationManager = new NKSKToolsNavigationManager(activity, R.id.nkSKMapContainer);
        mNKSKToolsNavigationManager.setNavigationListener(this);
        mNKSKToolsNavigationManager.setNavigationStateListener(this);
        mSKMapViewHolder = ((NKSkobblerMapFragment) nkMapFragment).getMapHolder();
    }

    @Override
    public void startNavigation() {
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

    @Override
    public void onSKNavigationStateUpdate(SKNavigationState skNavigationState) {
        NKBus.get().post(new NavigationStateUpdateEvent(
                NKSkobblerNavigationUtil.createNavigationState(skNavigationState)
        ));
    }
}

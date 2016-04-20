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
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;

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
        for (NKNavigationListener listener : mNavigationListeners) {
            listener.onNavigationStart();
        }
    }

    @Override
    public void onNavigationEnded() {
        for (NKNavigationListener listener : mNavigationListeners) {
            listener.onNavigationStop();
        }
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
        for (NKNavigationListener listener : mNavigationListeners) {
            listener.onNavigationStateUpdate(
                    NKSkobblerNavigationUtil.createNavigationState(skNavigationState)
            );
        }
    }
}

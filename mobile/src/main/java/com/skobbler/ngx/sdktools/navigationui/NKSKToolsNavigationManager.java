package com.skobbler.ngx.sdktools.navigationui;

import android.app.Activity;
import com.skobbler.ngx.map.SKMapViewHolder;

public class NKSKToolsNavigationManager extends SKToolsNavigationManager {

    public NKSKToolsNavigationManager(Activity activity, int rootId) {
        super(activity, rootId);
        NKSKToolsLogicManager.getInstance().setActivity(activity, rootId);
    }

    public void launchRouteCalculation(SKToolsNavigationConfiguration configuration, SKMapViewHolder mapHolder) {
        NKSKToolsLogicManager.getInstance().calculateRoute(configuration, mapHolder);
    }

    public void removeRouteCalculationScreen(){
        NKSKToolsLogicManager.getInstance().removeRouteCalculationScreen();
    }

    public void startNavigation(SKToolsNavigationConfiguration configuration, SKMapViewHolder mapHolder) {
        NKSKToolsLogicManager.getInstance().startNavigation(configuration, mapHolder, false);
    }

    public void stopNavigation() {
        NKSKToolsLogicManager.getInstance().stopNavigation();
    }

    public void startFreeDriveWithConfiguration(SKToolsNavigationConfiguration configuration,
                                                SKMapViewHolder mapHolder) {
        NKSKToolsLogicManager.getInstance().startNavigation(configuration, mapHolder, true);
    }

    public void notifyOrientationChanged() {
        NKSKToolsLogicManager.getInstance().notifyOrientationChanged();
    }

    public void setNavigationListener(SKToolsNavigationListener navigationListener) {
        NKSKToolsLogicManager.getInstance().setNavigationListener(navigationListener);
    }

    public void setNavigationStateListener(NKSKNavigationStateListener listener) {
        NKSKToolsLogicManager.getInstance().setNavigationStateListener(listener);
    }
}

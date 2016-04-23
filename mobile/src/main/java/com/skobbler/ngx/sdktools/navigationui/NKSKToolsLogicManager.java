package com.skobbler.ngx.sdktools.navigationui;

import com.skobbler.ngx.map.SKScreenPoint;
import com.skobbler.ngx.navigation.SKNavigationState;
import hk.gavin.navik.application.NKBus;

public class NKSKToolsLogicManager extends SKToolsLogicManager {

    protected NKSKToolsLogicManager() {
        super();
    }

    @Override
    public void onUpdateNavigationState(SKNavigationState skNavigationState) {
        super.onUpdateNavigationState(skNavigationState);
        NKBus.get().post(skNavigationState);
    }

    @Override
    public void onSingleTap(SKScreenPoint skScreenPoint) {
        // Do nothing
    }
}

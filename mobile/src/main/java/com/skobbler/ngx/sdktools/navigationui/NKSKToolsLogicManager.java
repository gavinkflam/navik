package com.skobbler.ngx.sdktools.navigationui;

import com.google.common.base.Optional;
import com.skobbler.ngx.navigation.SKNavigationState;

public class NKSKToolsLogicManager extends SKToolsLogicManager {

    Optional<NKSKNavigationStateListener> mNavigationStateListener = Optional.absent();

    private static volatile NKSKToolsLogicManager instance = null;

    public static NKSKToolsLogicManager getInstance() {
        if (instance == null) {
            synchronized (NKSKToolsLogicManager.class) {
                if (instance == null) {
                    instance = new NKSKToolsLogicManager();
                }
            }
        }
        return instance;
    }

    protected NKSKToolsLogicManager() {
        super();
    }

    public void setNavigationStateListener(NKSKNavigationStateListener listener) {
        mNavigationStateListener = Optional.of(listener);
    }

    @Override
    public void onUpdateNavigationState(SKNavigationState skNavigationState) {
        super.onUpdateNavigationState(skNavigationState);

        if (mNavigationStateListener.isPresent()) {
            mNavigationStateListener.get().onSKNavigationStateUpdate(skNavigationState);
        }
    }
}

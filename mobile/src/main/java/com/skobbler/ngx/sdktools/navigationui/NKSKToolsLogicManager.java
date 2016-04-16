package com.skobbler.ngx.sdktools.navigationui;

public class NKSKToolsLogicManager extends SKToolsLogicManager {

    private static volatile NKSKToolsLogicManager instance = null;

    public static NKSKToolsLogicManager getInstance() {
        if (instance == null) {
            synchronized (SKToolsLogicManager.class) {
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
}

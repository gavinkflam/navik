package hk.gavin.navik.application;

import com.google.common.eventbus.EventBus;

public final class NKBus {

    private static EventBus mBus;

    public static synchronized EventBus get() {
        if (mBus == null) {
            mBus = new EventBus();
        }
        return mBus;
    }
}

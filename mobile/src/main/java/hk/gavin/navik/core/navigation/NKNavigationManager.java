package hk.gavin.navik.core.navigation;

import android.app.Activity;
import hk.gavin.navik.core.map.NKMapFragment;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "m")
public abstract class NKNavigationManager {

    @Getter private Activity mActivity;
    @Getter private int mContainerId;
    @Getter private NKMapFragment mNKMapFragment;

    protected List<NKNavigationListener> mNavigationListeners = new ArrayList<>();

    public NKNavigationManager(Activity activity, int containerId, NKMapFragment nkMapFragment) {
        mActivity = activity;
        mContainerId = containerId;
        mNKMapFragment = nkMapFragment;
    }

    public void addNavigationListener(NKNavigationListener listener) {
        mNavigationListeners.add(listener);
    }

    public void removeNavigationListener(NKNavigationListener listener) {
        mNavigationListeners.remove(listener);
    }

    public abstract void startNavigation();
    public abstract void stopNavigation();
}

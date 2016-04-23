package hk.gavin.navik.core.navigation;

import android.app.Activity;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.map.NKMapFragment;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class NKNavigationManager {

    @Getter private Activity mActivity;
    @Getter private int mContainerId;
    @Getter private NKMapFragment mNKMapFragment;
    @Getter @Setter private boolean mSimulation;

    public NKNavigationManager(Activity activity, int containerId, NKMapFragment nkMapFragment) {
        mActivity = activity;
        mContainerId = containerId;
        mNKMapFragment = nkMapFragment;
    }

    public abstract void startNavigation(NKDirections directions);
    public abstract void stopNavigation();
}

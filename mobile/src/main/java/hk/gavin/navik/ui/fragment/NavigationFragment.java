package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import hk.gavin.navik.R;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.navigation.NKNavigationListener;
import hk.gavin.navik.core.navigation.NKNavigationManager;
import hk.gavin.navik.core.navigation.NKNavigationState;
import hk.gavin.navik.core.navigation.NKSkobblerNavigationManager;
import hk.gavin.navik.core.wear.NKWearManager;
import hk.gavin.navik.preference.MainPreferences;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class NavigationFragment extends AbstractHomeUiFragment implements NKNavigationListener {

    @Inject MainPreferences mMainPreferences;
    @Inject NKLocationProvider mLocationProvider;
    @Inject NKWearManager mWearManager;
    NKMapFragment mMap;
    NKNavigationManager mNavigationManager;

    @Getter private final int mLayoutResId = R.layout.fragment_navigation;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Properly set map display
        mMap = getController().getMap();
        mMap.hideMoveToCurrentLocationButton();

        // Create navigation manager and start navigation
        mNavigationManager = new NKSkobblerNavigationManager(getActivity(), R.id.nkSKMapContainer, mMap);
        mNavigationManager.addNavigationListener(this);
        mNavigationManager.startNavigation();

        // Update title and back button display
        getController().setActionBarTitle(R.string.navigation);
        getController().setDisplayHomeAsUp(true);
    }

    @Override
    public void onBackPressed() {
        mNavigationManager.stopNavigation();
        mNavigationManager.removeNavigationListener(this);
    }

    @Override
    public void onStop() {
        mNavigationManager.stopNavigation();
        mNavigationManager.removeNavigationListener(this);
        super.onStop();
    }

    @Override
    public void onNavigationStart() {
        mWearManager.startWearActivity();
    }

    @Override
    public void onNavigationStop() {
        getController().goBack();
    }

    @Override
    public void onNavigationStateUpdate(NKNavigationState navigationState) {
        mWearManager.broadcastNavigationState(navigationState);
    }
}

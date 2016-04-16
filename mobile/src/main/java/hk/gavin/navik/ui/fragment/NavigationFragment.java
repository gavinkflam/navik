package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import hk.gavin.navik.R;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.navigation.NKNavigationManager;
import hk.gavin.navik.core.navigation.NKSkobblerNavigationManager;
import hk.gavin.navik.preference.MainPreferences;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class NavigationFragment extends AbstractHomeUiFragment implements NKMapFragment.MapEventsListener {

    @Inject MainPreferences mMainPreferences;
    @Inject NKLocationProvider mLocationProvider;
    NKMapFragment mMap;
    NKNavigationManager mNavigationManager;

    @Getter private final int mLayoutResId = R.layout.fragment_navigation;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Properly set map display
        mMap = getController().getMap();
        mMap.hideMoveToCurrentLocationButton();
        mMap.setMapEventsListener(this);

        // Create navigation manager and start navigation
        mNavigationManager = new NKSkobblerNavigationManager(getActivity(), R.id.nkSKMapContainer, mMap);
        mNavigationManager.startNavigation();

        // Update title and back button display
        getController().setActionBarTitle(R.string.navigation);
        getController().setDisplayHomeAsUp(true);
    }

    @Override
    public void onBackPressed() {
        mNavigationManager.stopNavigation();
    }

    @Override
    public void onStop() {
        mNavigationManager.stopNavigation();
        super.onStop();
    }

    @Override
    public void onMapLoadComplete() {
        // Do nothing
    }
}

package hk.gavin.navik.ui.fragment;

import hk.gavin.navik.R;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.preference.MainPreferences;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class NavigationFragment extends AbstractHomeUiFragment implements NKMapFragment.MapEventsListener {

    @Inject MainPreferences mMainPreferences;
    @Inject NKLocationProvider mLocationProvider;
    NKMapFragment mMap;

    @Getter private final int mLayoutResId = R.layout.fragment_navigation;

    @Override
    public void onInitialize() {
        if (isActivityCreated() && !isInitialized()) {
            mMap = getController().getMap();
            super.onInitialize();
        }
    }

    @Override
    public void onInitializeViews() {
        if (isInitialized() && !isViewsInitialized()) {
            mMap.startNavigation();
            super.onInitializeViews();
        }
    }

    @Override
    public void onViewVisible() {
        if (isInitialized()) {
            getController().setActionBarTitle(R.string.navigation);
            getController().setDisplayHomeAsUp(true);

            mMap.hideMoveToCurrentLocationButton();
            mMap.setMapEventsListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        mMap.stopNavigation();
    }

    @Override
    public void onStop() {
        mMap.stopNavigation();
        super.onStop();
    }

    @Override
    public void onMapLoadComplete() {
        // Do nothing
    }
}

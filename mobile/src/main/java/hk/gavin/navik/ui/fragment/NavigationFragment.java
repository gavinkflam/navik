package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import com.google.common.eventbus.Subscribe;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.map.event.MapLoadCompleteEvent;
import hk.gavin.navik.core.navigation.NKNavigationManager;
import hk.gavin.navik.core.navigation.NKSkobblerNavigationManager;
import hk.gavin.navik.core.navigation.event.NavigationEndedEvent;
import hk.gavin.navik.core.navigation.event.NavigationStartedEvent;
import hk.gavin.navik.core.navigation.event.NavigationStateUpdateEvent;
import hk.gavin.navik.core.wear.NKWearManager;
import hk.gavin.navik.preference.MainPreferences;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class NavigationFragment extends AbstractNavigationUiFragment {

    @Inject MainPreferences mMainPreferences;
    @Inject NKLocationProvider mLocationProvider;
    @Inject NKWearManager mWearManager;

    NKMapFragment mMap;
    NKNavigationManager mNavigationManager;
    NKDirections mDirections;

    @Getter private final int mLayoutResId = R.layout.fragment_navigation;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get map and directions
        mMap = getController().getMap();
        mDirections = (NKDirections) getActivity().getIntent().getSerializableExtra(UiContract.DataKey.DIRECTIONS);

        // Update title and back button display
        getController().setActionBarTitle(R.string.navigation);
        getController().setDisplayHomeAsUp(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        NKBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        NKBus.get().unregister(this);
        mWearManager.stopWearActivity();
        mNavigationManager.stopNavigation();
        super.onDestroy();
    }

    @Subscribe
    public void onMapLoadCompleted(MapLoadCompleteEvent event) {
        mMap.hideMoveToCurrentLocationButton();

        // Create navigation manager and start navigation
        mNavigationManager = new NKSkobblerNavigationManager(getActivity(), R.id.nkSKMapContainer, mMap);
        mNavigationManager.setSimulation(mMainPreferences.getSimulationMode());
        mNavigationManager.startNavigation(mDirections);
    }

    @Subscribe
    public void onNavigationStarted(NavigationStartedEvent event) {
        mWearManager.startWearActivity();
    }

    @Subscribe
    public void onNavigationEnded(NavigationEndedEvent event) {
        mWearManager.stopWearActivity();
        getActivity().finish();
    }

    @Subscribe
    public void onNavigationStateUpdate(NavigationStateUpdateEvent event) {
        mWearManager.broadcastNavigationState(event.navigationState);
    }
}

package hk.gavin.navik.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.SKMapViewStyle;
import com.skobbler.ngx.navigation.SKNavigationSettings;
import com.skobbler.ngx.routing.*;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationConfiguration;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationListener;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationManager;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.NavigationActivity;
import hk.gavin.navik.map.NavikMapFragment;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Inject;

public class NavigationFragment extends Fragment implements NavikMapFragment.MapEventsListener {

    @Inject MainPreferences mMainPreferences;

    NavikMapFragment mNavikMapFragment;

    private NavigationListener mNavigationListener = new NavigationListener();
    private SKToolsNavigationManager mNavigationManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((NavigationActivity) getActivity()).component().inject(this);
        mNavigationManager =  new SKToolsNavigationManager(getActivity(), R.id.navigationRoot);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mNavikMapFragment = (NavikMapFragment) getChildFragmentManager().findFragmentById(R.id.navigationMap);
        mNavikMapFragment.setMapEventsListener(this);
    }

    @Override
    public void onStop() {
        mNavigationManager.stopNavigation();
        super.onStop();
    }

    @Override
    public void onMapLoadComplete() {
        startNavigationSimulation();
    }

    private void startNavigationSimulation() {
        SKToolsNavigationConfiguration configuration = new SKToolsNavigationConfiguration();
        configuration.setStartCoordinate(new SKCoordinate(114.1707187, 22.4440508));
        configuration.setDestinationCoordinate(new SKCoordinate(114.2353906, 22.4660219));
        configuration.setDayStyle(
                new SKMapViewStyle(mMainPreferences.getMapResourcesPath() + ".DayStyle/", "daystyle.json")
        );
        configuration.setNightStyle(
                new SKMapViewStyle(mMainPreferences.getMapResourcesPath() + ".NightStyle/", "nightstyle.json")
        );
        configuration.setNavigationType(SKNavigationSettings.SKNavigationType.SIMULATION);
        configuration.setRouteType(SKRouteSettings.SKRouteMode.BICYCLE_QUIETEST);

        mNavigationManager.setNavigationListener(mNavigationListener);
        mNavigationManager.startNavigation(configuration, mNavikMapFragment.getMapHolder());
    }

    private class NavigationListener implements SKToolsNavigationListener {

        @Override
        public void onNavigationStarted() {

        }

        @Override
        public void onNavigationEnded() {

        }

        @Override
        public void onRouteCalculationStarted() {

        }

        @Override
        public void onRouteCalculationCompleted() {

        }

        @Override
        public void onRouteCalculationCanceled() {

        }
    }
}

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

    private RouteCalculationListener mRouteCalculationListener = new RouteCalculationListener();
    private NavigationListener mNavigationListener = new NavigationListener();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((NavigationActivity) getActivity()).component().inject(this);
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
    public void onMapLoadComplete() {
        startNavigationSimulation();
    }

    private void startNavigationSimulation() {
        SKRouteSettings route = new SKRouteSettings();

        route.setStartCoordinate(new SKCoordinate(114.1707187, 22.4440508));
        route.setDestinationCoordinate(new SKCoordinate(114.2353906, 22.4660219));

        /*
        route.setDestinationCoordinate(new SKCoordinate(114.1957007, 22.3955298));

        ArrayList<SKViaPoint> viaPoints = new ArrayList<>();
        viaPoints.add(new SKViaPoint(0, new SKCoordinate(114.1783895, 22.4452662)));
        viaPoints.add(new SKViaPoint(1, new SKCoordinate(114.2132063, 22.414166)));
        route.setViaPoints(viaPoints);
        */

        route.setNoOfRoutes(1);
        route.setRouteMode(SKRouteSettings.SKRouteMode.BICYCLE_QUIETEST);

        route.setAvoidFerries(true);
        route.setBicycleCarryAvoided(true);
        route.setBicycleWalkAvoided(true);
        route.setHighWaysAvoided(true);
        route.setTollRoadsAvoided(true);
        route.setUseRoadSlopes(true);
        route.setFilterAlternatives(true);
        route.setRouteExposed(true);

        SKRouteManager.getInstance().setRouteListener(mRouteCalculationListener);
        SKRouteManager.getInstance().calculateRoute(route);
    }

    private class RouteCalculationListener implements SKRouteListener {

        @Override
        public void onRouteCalculationCompleted(SKRouteInfo skRouteInfo) {

        }

        @Override
        public void onRouteCalculationFailed(SKRoutingErrorCode skRoutingErrorCode) {

        }

        @Override
        public void onAllRoutesCompleted() {
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

            SKToolsNavigationManager navigationManager = new SKToolsNavigationManager(getActivity(), R.id.navigationRoot);
            navigationManager.setNavigationListener(mNavigationListener);
            navigationManager.startNavigation(configuration, mNavikMapFragment.getMapHolder());
        }

        @Override
        public void onServerLikeRouteCalculationCompleted(SKRouteJsonAnswer skRouteJsonAnswer) {

        }

        @Override
        public void onOnlineRouteComputationHanging(int i) {

        }
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

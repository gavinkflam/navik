package hk.gavin.navik.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.navigation.SKNavigationListener;
import com.skobbler.ngx.navigation.SKNavigationManager;
import com.skobbler.ngx.navigation.SKNavigationSettings;
import com.skobbler.ngx.navigation.SKNavigationState;
import com.skobbler.ngx.routing.*;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.NavigationActivity;
import hk.gavin.navik.map.NavikMapFragment;

public class NavigationFragment extends Fragment implements NavikMapFragment.MapEventsListener {

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
            SKNavigationSettings navigationSettings = new SKNavigationSettings();
            navigationSettings.setNavigationType(SKNavigationSettings.SKNavigationType.SIMULATION);
            navigationSettings.setNavigationMode(SKNavigationSettings.SKNavigationMode.BIKE);

            SKNavigationManager navigationManager = SKNavigationManager.getInstance();
            navigationManager.setMapView(mNavikMapFragment.getMap());
            navigationManager.setNavigationListener(mNavigationListener);
            navigationManager.startNavigation(navigationSettings);
        }

        @Override
        public void onServerLikeRouteCalculationCompleted(SKRouteJsonAnswer skRouteJsonAnswer) {

        }

        @Override
        public void onOnlineRouteComputationHanging(int i) {

        }
    }

    private class NavigationListener implements SKNavigationListener {

        @Override
        public void onDestinationReached() {

        }

        @Override
        public void onSignalNewAdviceWithInstruction(String s) {

        }

        @Override
        public void onSignalNewAdviceWithAudioFiles(String[] strings, boolean b) {

        }

        @Override
        public void onSpeedExceededWithAudioFiles(String[] strings, boolean b) {

        }

        @Override
        public void onSpeedExceededWithInstruction(String s, boolean b) {

        }

        @Override
        public void onUpdateNavigationState(SKNavigationState skNavigationState) {

        }

        @Override
        public void onReRoutingStarted() {

        }

        @Override
        public void onFreeDriveUpdated(String s, String s1, String s2, SKNavigationState.SKStreetType skStreetType, double v, double v1) {

        }

        @Override
        public void onViaPointReached(int i) {

        }

        @Override
        public void onVisualAdviceChanged(boolean b, boolean b1, SKNavigationState skNavigationState) {

        }

        @Override
        public void onTunnelEvent(boolean b) {

        }
    }
}

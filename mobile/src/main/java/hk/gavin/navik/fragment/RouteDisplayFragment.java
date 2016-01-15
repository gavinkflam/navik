package hk.gavin.navik.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.routing.*;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.ui.HomeController;

import javax.inject.Inject;

public class RouteDisplayFragment extends AbstractUiFragment implements NKMapFragment.MapEventsListener {

    @Inject NKLocationProvider mLocationProvider;
    @Inject HomeController mController;
    NKMapFragment mMap;

    private SKRouteManager mRouteManager = SKRouteManager.getInstance();
    private RouteHandler mRouteHandler = new RouteHandler();

    private SKRouteSettings mRoute;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).component().inject(this);
        initializeFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_display, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initializeFragments();
    }

    @Override
    public void onResume() {
        super.onResume();
        onViewVisible();
    }

    @Override
    public void onViewVisible() {
        if (mMap == null) {
            return;
        }

        mMap.hideMoveToCurrentLocationButton();
        mMap.setMapEventsListener(this);
    }

    private void initializeFragments() {
        if (mController == null) {
            return;
        }

        mMap = mController.getMap();
        mMap.moveToCurrentLocationOnceAvailable();
        onViewVisible();
    }

    @Override
    public void onMapLoadComplete() {
        mRoute = new SKRouteSettings();
        mRoute.setStartCoordinate(new SKCoordinate(114.162574, 22.434520));
        mRoute.setDestinationCoordinate(new SKCoordinate(114.213902, 22.455445));

        /*
        mRoute.setDestinationCoordinate(new SKCoordinate(114.1957007, 22.3955298));

        ArrayList<SKViaPoint> viaPoints = new ArrayList<>();
        viaPoints.add(new SKViaPoint(0, new SKCoordinate(114.1783895, 22.4452662)));
        viaPoints.add(new SKViaPoint(1, new SKCoordinate(114.2132063, 22.414166)));
        mRoute.setViaPoints(viaPoints);
        */

        mRoute.setNoOfRoutes(1);
        mRoute.setRouteMode(SKRouteSettings.SKRouteMode.BICYCLE_QUIETEST);

        mRoute.setAvoidFerries(true);
        mRoute.setBicycleCarryAvoided(true);
        mRoute.setBicycleWalkAvoided(true);
        mRoute.setHighWaysAvoided(true);
        mRoute.setTollRoadsAvoided(true);
        mRoute.setUseRoadSlopes(true);
        mRoute.setFilterAlternatives(true);
        mRoute.setRouteExposed(true);

        mRouteManager.setRouteListener(mRouteHandler);
        mRouteManager.calculateRoute(mRoute);
    }

    private class RouteHandler implements SKRouteListener {

        @Override
        public void onRouteCalculationCompleted(SKRouteInfo skRouteInfo) {

        }

        @Override
        public void onRouteCalculationFailed(SKRoutingErrorCode skRoutingErrorCode) {

        }

        @Override
        public void onAllRoutesCompleted() {
            mRouteManager.zoomMapToCurrentRoute();
        }

        @Override
        public void onServerLikeRouteCalculationCompleted(SKRouteJsonAnswer skRouteJsonAnswer) {

        }

        @Override
        public void onOnlineRouteComputationHanging(int i) {

        }
    }
}

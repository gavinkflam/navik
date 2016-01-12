package hk.gavin.navik.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.routing.*;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.map.NavikMapFragment;

public class RouteDisplayFragment extends Fragment implements NavikMapFragment.MapEventsListener {

    NavikMapFragment mNavikMapFragment;

    private SKRouteManager mRouteManager = SKRouteManager.getInstance();
    private RouteHandler mRouteHandler = new RouteHandler();

    private SKRouteSettings mRoute;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_display, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mNavikMapFragment = (NavikMapFragment) getChildFragmentManager().findFragmentById(R.id.routeDisplayMap);
        mNavikMapFragment.hideMoveToCurrentLocationButton();
        mNavikMapFragment.moveToCurrentLocationOnceAvailable();
        mNavikMapFragment.setMapEventsListener(this);
    }

    @Override
    public void onMapLoadComplete() {
        mRoute = new SKRouteSettings();
        mRoute.setStartCoordinate(new SKCoordinate(114.1707187, 22.4440508));
        mRoute.setDestinationCoordinate(new SKCoordinate(114.2353906, 22.4660219));

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

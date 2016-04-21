package hk.gavin.navik.ui.fragmentcontroller;

import android.content.Intent;
import android.widget.Toast;
import hk.gavin.navik.R;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.fragment.*;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class HomeFragmentController extends FragmentController<HomeActivity> {

    @Getter private NKMapFragment mMap;

    public HomeFragmentController(HomeActivity activity) {
        super(activity);
    }

    public void initialize() {
        super.initialize();

        setEmptyRequest();
        replaceFragment(R.id.contentFrame, RoutePlannerFragment.class, UiContract.FragmentTag.HOME, false, false);
        mMap = replaceFragment(
                R.id.homeMap, NKSkobblerMapFragment.class, UiContract.FragmentTag.HOME_MAP, false, false
        );
        getActivity().component().inject((NKSkobblerMapFragment) mMap);
    }

    public void showMessage(String notification) {
        Toast.makeText(getActivity(), notification, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(int notificationRes) {
        showMessage(getActivity().getString(notificationRes));
    }

    public RouteDisplayFragment initializeRouteDisplayFragment() {
        setEmptyRequest();
        return replaceFragment(
                R.id.routePlannerContentFrame, RouteDisplayFragment.class, UiContract.FragmentTag.ROUTE_DISPLAY,
                false, true
        );
    }

    public void selectStartingPoint() {
        setRequest(UiContract.RequestCode.STARTING_POINT_LOCATION);
        replaceFragment(
                R.id.contentFrame, LocationSelectionFragment.class, UiContract.FragmentTag.SELECT_STARTING_POINT,
                true, true
        );
    }

    public void selectDestination() {
        setRequest(UiContract.RequestCode.DESTINATION_LOCATION);
        replaceFragment(
                R.id.contentFrame, LocationSelectionFragment.class, UiContract.FragmentTag.SELECT_DESTINATION,
                true, true
        );
    }

    public void showRouteAnalysis(NKDirections directions) {
        Intent data = new Intent();
        data.putExtra(UiContract.DataKey.DIRECTIONS, directions);
        setRequest(UiContract.RequestCode.DEFAULT, data);

        replaceFragment(
                R.id.contentFrame, RouteAnalysisFragment.class, UiContract.FragmentTag.ROUTE_ANALYSIS,
                true, true
        );
    }

    public void showAppSetting() {
        setEmptyRequest();
        replaceFragment(
                R.id.contentFrame, SettingFragment.class, UiContract.FragmentTag.SETTING,
                true, true
        );
    }

    public void startBikeNavigation(NKDirections directions) {
        Intent data = new Intent();
        data.putExtra(UiContract.DataKey.DIRECTIONS, directions);
        setRequest(UiContract.RequestCode.DEFAULT, data);

        replaceFragment(
                R.id.contentFrame, NavigationFragment.class, UiContract.FragmentTag.NAVIGATION,
                true, true
        );
    }
}

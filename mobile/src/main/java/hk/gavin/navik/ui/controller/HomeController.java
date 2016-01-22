package hk.gavin.navik.ui.controller;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.contract.UiContract;
import hk.gavin.navik.ui.fragment.*;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class HomeController implements FragmentManager.OnBackStackChangedListener {

    private HomeActivity mActivity;
    private FragmentManager mManager;
    @Getter private NKMapFragment mMap;
    @Getter private AbstractUiFragment mCurrentFragment;

    @Getter private int mRequestCode;
    private int mResultCode = UiContract.ResultCode.NA;
    private Intent mResultData;

    public HomeController(HomeActivity activity) {
        mActivity = activity;
        mManager = mActivity.getSupportFragmentManager();
    }

    public void initialize() {
        mManager.addOnBackStackChangedListener(this);

        replaceFragment(R.id.contentFrame, RoutePlannerFragment.class, UiContract.FragmentTag.HOME, false, false);
        mMap = replaceFragment(
                R.id.homeMap, NKSkobblerMapFragment.class, UiContract.FragmentTag.HOME_MAP, false, false
        );
    }

    public RouteDisplayFragment initializeRouteDisplayFragment() {
        return replaceFragment(
                R.id.routePlannerContentFrame, RouteDisplayFragment.class, UiContract.FragmentTag.ROUTE_DISPLAY,
                false, true
        );
    }

    public void selectStartingPoint() {
        mRequestCode = UiContract.RequestCode.STARTING_POINT_LOCATION;
        replaceFragment(
                R.id.contentFrame, LocationSelectionFragment.class, UiContract.FragmentTag.SELECT_STARTING_POINT,
                true, true
        );
    }

    public void selectDestination() {
        mRequestCode = UiContract.RequestCode.DESTINATION_LOCATION;
        replaceFragment(
                R.id.contentFrame, LocationSelectionFragment.class, UiContract.FragmentTag.SELECT_DESTINATION,
                true, true
        );
    }

    public void startBikeNavigation() {
        replaceFragment(
                R.id.contentFrame, NavigationFragment.class, UiContract.FragmentTag.NAVIGATION,
                true, true
        );
    }

    public void setResultData(int resultCode, Intent result) {
        mResultCode = resultCode;
        mResultData = result;
    }

    public void goBack() {
        mActivity.onBackPressed();
    }

    public void setActionBarTitle(@StringRes int stringRes) {
        getActionBar().setTitle(stringRes);
    }

    public void setDisplayHomeAsUp(boolean displayHomeAsUp) {
        getActionBar().setDisplayShowHomeEnabled(displayHomeAsUp);
        getActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUp);
    }

    public <T extends Fragment> T replaceFragment(
            int id, Class<T> fClass, String tag, boolean transition, boolean addToBackStack) {
        FragmentTransaction transaction = mManager.beginTransaction();
        T fragment = getFragment(fClass, tag);

        transaction.replace(id, fragment, tag);
        if (transition) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        if (addToBackStack) {
            transaction.addToBackStack(UiContract.FragmentTag.SELECT_STARTING_POINT);
        }
        transaction.commit();

        return fragment;
    }

    public <T extends Fragment> T getFragment(Class<T> fClass, String tag) {
        Optional<T> fragment = Optional.fromNullable(
                (T) mManager.findFragmentByTag(tag)
        );
        if (!fragment.isPresent()) {
            try {
                fragment = Optional.of(fClass.newInstance());
            }
            catch (Exception e) {
                // Do nothing
            }
        }

        return fragment.get();
    }

    @Override
    public void onBackStackChanged() {
        Optional<Fragment> fragment = Optional.fromNullable(
                mManager.findFragmentById(R.id.contentFrame)
        );
        if (fragment.isPresent()) {
            mCurrentFragment = (AbstractUiFragment) fragment.get();
            if (mResultCode != UiContract.ResultCode.NA) {
                mCurrentFragment.onResultAvailable(mRequestCode, mResultCode, mResultData);
                mResultCode = UiContract.ResultCode.NA;
            }
        }
    }

    private ActionBar getActionBar() {
        return mActivity.getSupportActionBar();
    }
}

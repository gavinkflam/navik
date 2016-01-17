package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import hk.gavin.navik.R;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.preference.MainPreferences;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.controller.HomeController;

import javax.inject.Inject;

public class NavigationFragment extends AbstractUiFragment implements NKMapFragment.MapEventsListener {

    @Inject MainPreferences mMainPreferences;
    @Inject HomeController mController;
    @Inject NKLocationProvider mLocationProvider;

    NKMapFragment mMap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).component().inject(this);
        initializeFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeFragments();
    }

    @Override
    public void onViewVisible() {
        mMap.hideMoveToCurrentLocationButton();
        mMap.setMapEventsListener(this);

        ActionBar actionBar = mController.getActionBar();
        actionBar.setTitle(R.string.navigation);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mMap.startNavigation();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mMap.stopNavigation();
                mController.goBack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mMap.stopNavigation();
    }

    private void initializeFragments() {
        if (isActivityCreated()) {
            mMap = mController.getMap();
            onViewVisible();
        }
    }
}

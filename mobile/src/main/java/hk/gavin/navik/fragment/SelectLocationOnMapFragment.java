package hk.gavin.navik.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.ui.HomeController;

import javax.inject.Inject;

public class SelectLocationOnMapFragment extends AbstractUiFragment {

    @Inject NKLocationProvider mLocationProvider;
    @Inject HomeController mController;

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
        return inflater.inflate(R.layout.fragment_select_location_on_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initializeFragments();
    }

    @Override
    public void onViewVisible() {
        mMap.showMoveToCurrentLocationButton();

        ActionBar actionBar = mController.getActionBar();
        if (actionBar != null) {
            switch (mController.getRequestCode()) {
                case UiContract.RequestCode.STARTING_POINT_LOCATION: {
                    actionBar.setTitle(R.string.select_starting_point);
                    break;
                }
                case UiContract.RequestCode.DESTINATION_LOCATION: {
                    actionBar.setTitle(R.string.select_destination);
                    break;
                }
            }
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeFragments() {
        if (mController == null) {
            return;
        }

        mMap = mController.getMap();
        onViewVisible();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mController.goBack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.confirmLocationSelection)
    void confirmLocationSelection() {
        Intent result = new Intent();
        result.putExtra(UiContract.DataKey.LOCATION, mMap.getMapCenter());

        mController.setResultData(UiContract.ResultCode.OK, result);
        mController.goBack();
    }
}

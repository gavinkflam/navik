package hk.gavin.navik.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.contract.UiContract;
import hk.gavin.navik.ui.controller.HomeController;

import javax.inject.Inject;

public class LocationSelectionFragment extends AbstractUiFragment {

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
        return inflater.inflate(R.layout.fragment_location_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeFragments();
    }

    @Override
    public void onViewVisible() {
        mController.setActionBarTitle(
                mController.getRequestCode() == UiContract.RequestCode.STARTING_POINT_LOCATION ?
                        R.string.select_starting_point : R.string.select_destination
        );
        mController.setDisplayHomeAsUp(true);

        mMap.showMoveToCurrentLocationButton();
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

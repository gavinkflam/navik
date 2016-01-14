package hk.gavin.navik.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.activity.SelectLocationOnMapActivity;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.widget.LocationSelector;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    @Inject NKLocationProvider mLocationProvider;
    @Inject NKReverseGeocoder mReverseGeocoder;

    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;

    LocationSelectorController mLocationSelectorController= new LocationSelectorController();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).component().inject(this);
        initializeViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initializeViews();
    }

    private void initializeViews() {
        if (mLocationProvider == null || mStartingPoint == null) {
            return;
        }

        mStartingPoint.initialize(mLocationProvider, mReverseGeocoder);
        mDestination.initialize(mLocationProvider, mReverseGeocoder);
        mStartingPoint.useCurrentLocation();
        mDestination.setLocation(null);

        mStartingPoint.setOnLocationUpdatedListener(mLocationSelectorController);
        mStartingPoint.setOnMenuItemClickListener(mLocationSelectorController);

        mDestination.setOnLocationUpdatedListener(mLocationSelectorController);
        mDestination.setOnMenuItemClickListener(mLocationSelectorController);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UiContract.RequestCode.STARTING_POINT_LOCATION: {
                if (resultCode == UiContract.ResultCode.OK) {
                    NKLocation location = (NKLocation) data.getSerializableExtra(UiContract.DataKey.LOCATION);
                    mStartingPoint.setLocation(location);
                }
                break;
            }
            case UiContract.RequestCode.DESTINATION_LOCATION: {
                if (resultCode == UiContract.ResultCode.OK) {
                    NKLocation location = (NKLocation) data.getSerializableExtra(UiContract.DataKey.LOCATION);
                    mDestination.setLocation(location);
                }
                break;
            }
        }
    }

    private class LocationSelectorController implements LocationSelector.OnLocationUpdatedListener,
            LocationSelector.OnMenuItemClickListener {

        @Override
        public void onLocationUpdated(LocationSelector selector, NKLocation location) {

        }

        @Override
        public void onCurrentLocationClicked(LocationSelector selector) {
            switch (selector.getId()) {
                case R.id.startingPoint: {
                    mStartingPoint.useCurrentLocation();
                    break;
                }
                case R.id.destination: {
                    mDestination.useCurrentLocation();
                    break;
                }
            }
        }

        @Override
        public void onHistoryClicked(LocationSelector selector) {

        }

        @Override
        public void onSelectLocationOnMapClicked(LocationSelector selector) {
            Intent startActivity = new Intent(HomeFragment.this.getContext(), SelectLocationOnMapActivity.class);
            switch (selector.getId()) {
                case R.id.startingPoint: {
                    startActivity.putExtra(UiContract.DataKey.TITLE, getString(R.string.select_destination));
                    startActivityForResult(startActivity, UiContract.RequestCode.STARTING_POINT_LOCATION);
                    break;
                }
                case R.id.destination: {
                    startActivity.putExtra(UiContract.DataKey.TITLE, getString(R.string.select_destination));
                    startActivityForResult(startActivity, UiContract.RequestCode.DESTINATION_LOCATION);
                    break;
                }
            }
        }
    }

}

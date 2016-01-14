package hk.gavin.navik.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.SelectLocationOnMapActivity;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;

import javax.inject.Inject;

public class SelectLocationOnMapFragment extends Fragment {

    @Inject NKLocationProvider mLocationProvider;
    NKMapFragment mMap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((SelectLocationOnMapActivity) getActivity()).component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_location_on_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mMap = (NKMapFragment) getChildFragmentManager().findFragmentById(R.id.locationSelectionMap);
        mMap.showMoveToCurrentLocationButton();
        mMap.moveToCurrentLocationOnceAvailable();
    }

    public NKLocation getLocation() {
        if (mMap != null) {
            return mMap.getMapCenter();
        }
        return null;
    }
}

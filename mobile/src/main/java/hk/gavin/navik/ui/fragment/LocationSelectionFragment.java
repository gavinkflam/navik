package hk.gavin.navik.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.ui.contract.UiContract;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class LocationSelectionFragment extends AbstractHomeUiFragment {

    @Inject NKLocationProvider mLocationProvider;
    NKMapFragment mMap;

    @Getter private final int mLayoutResId = R.layout.fragment_location_selection;

    @OnClick(R.id.confirmLocationSelection)
    void confirmLocationSelection() {
        Intent result = new Intent();
        result.putExtra(UiContract.DataKey.LOCATION, mMap.getMapCenter());

        getController().setResultData(UiContract.ResultCode.OK, result);
        getController().goBack();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Properly set map display
        mMap = getController().getMap();
        mMap.showMoveToCurrentLocationButton();

        // Update title and back button display
        getController().setActionBarTitle(
                getController().getRequestCode() == UiContract.RequestCode.STARTING_POINT_LOCATION ?
                        R.string.select_starting_point : R.string.select_destination
        );
        getController().setDisplayHomeAsUp(true);
    }
}

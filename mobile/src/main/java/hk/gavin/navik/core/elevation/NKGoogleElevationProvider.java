package hk.gavin.navik.core.elevation;

import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.ElevationResult;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationUtil;

import java.util.List;

public class NKGoogleElevationProvider extends NKElevationProvider implements PendingResult.Callback<ElevationResult[]> {

    private GeoApiContext mApiContext;

    public NKGoogleElevationProvider() {
        mApiContext = new GeoApiContext().setApiKey("AIzaSyBbT8ctaj6U3tEtArShI3XakgWIM6UsBO8");
    }

    public void requestElevation(List<NKLocation> locations) {
        ElevationApi
                .getByPoints(mApiContext, NKLocationUtil.toLatLngArray(locations))
                .setCallback(this);
    }

    @Override
    public void onResult(ElevationResult[] result) {

    }

    @Override
    public void onFailure(Throwable e) {

    }
}

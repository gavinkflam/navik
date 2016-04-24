package hk.gavin.navik.core.elevation;

import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.ElevationResult;
import com.orhanobut.logger.Logger;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationUtil;
import hk.gavin.navik.util.CollectionUtility;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.util.List;

public class NKGoogleElevationProvider implements NKElevationProvider {

    private GeoApiContext mApiContext;

    public NKGoogleElevationProvider() {
        mApiContext = new GeoApiContext().setApiKey("AIzaSyDnxUjoeFR5mnAOwP1eGown4PESZIDd4bE");
    }

    public Promise<NKLocation[], Void, Void> requestElevation(List<NKLocation> locations) {
        final DeferredObject<NKLocation[], Void, Void> def = new DeferredObject<>();

        ElevationApi
                .getByPoints(mApiContext, NKLocationUtil.toLatLngArray(locations))
                .setCallback(new PendingResult.Callback<ElevationResult[]>() {

                    @Override
                    public void onResult(ElevationResult[] result) {
                        Logger.d("ElevationResult.length(): %d", result.length);
                        def.resolve(NKLocationUtil.fromElevationResults(result));
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Logger.d("ElevationResult error: %s", e.toString());
                        def.reject(null);
                    }
                });
        return def.promise();
    }

    @Override
    public Promise<NKLocation[], Void, Void> requestElevation(List<NKLocation> locations, int limit) {
        return requestElevation(CollectionUtility.sampleIfExceedLimit(locations, limit));
    }
}

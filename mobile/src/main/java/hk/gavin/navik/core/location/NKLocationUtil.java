package hk.gavin.navik.core.location;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.LatLng;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.routing.SKViaPoint;

import java.util.ArrayList;
import java.util.List;

public class NKLocationUtil {

    public static NKLocation fromElevationResult(ElevationResult result) {
        return new NKLocation(result.location.lat, result.location.lng, result.elevation);
    }

    public static NKLocation[] fromElevationResults(ElevationResult[] results) {
        NKLocation[] locations = new NKLocation[results.length];
        for (int i = 0; i < results.length; i++) {
            locations[i] = fromElevationResult(results[i]);
        }

        return locations;
    }

    public static double[] extractElevations(NKLocation[] locations) {
        double[] elevations = new double[locations.length];
        for (int i = 0; i < locations.length; i++) {
            elevations[i] = locations[i].elevation;
        }
        return elevations;
    }

    public static List<SKCoordinate> toSKCoordinateList(List<NKLocation> locationList) {
        return Lists.transform(locationList, toSKCoordinateFunction);
    }

    public static List<SKViaPoint> toSKViaPointList(List<NKLocation> locationList) {
        List<SKViaPoint> viaPointList = new ArrayList<>();
        for (int i = 0; i < locationList.size(); i++) {
            viaPointList.add(new SKViaPoint(i, locationList.get(i).toSKCoordinate()));
        }
        return viaPointList;
    }

    public static ImmutableList<NKLocation> toImmutableNKLocationList(List<SKCoordinate> skCoordinateList) {
        ImmutableList.Builder<NKLocation> builder = new ImmutableList.Builder<>();
        for (SKCoordinate coordinate : skCoordinateList) {
            builder.add(NKLocation.fromSKCoordinate(coordinate));
        }
        return builder.build();
    }

    public static LatLng[] toLatLngArray(List<NKLocation> locationList) {
        LatLng[] latLngs = new LatLng[locationList.size()];
        for (int i = 0; i < locationList.size(); i++) {
            NKLocation location = locationList.get(i);
            latLngs[i] = new LatLng(location.latitude, location.longitude);
        }
        return latLngs;
    }

    public final static Function<NKLocation, SKCoordinate> toSKCoordinateFunction =
            new Function<NKLocation, SKCoordinate>() {

                @Override
                public SKCoordinate apply(NKLocation input) {
                    return input.toSKCoordinate();
                }
            };
}

package hk.gavin.navik.core.elevation;

import hk.gavin.navik.core.location.NKLocation;
import org.jdeferred.Promise;

import java.util.List;

public interface NKElevationProvider {

    Promise<NKLocation[], Void, Void> requestElevation(List<NKLocation> locations);
    Promise<NKLocation[], Void, Void> requestElevation(List<NKLocation> locations, int limit);
}

package hk.gavin.navik.core.geocode;

import com.skobbler.ngx.reversegeocode.SKReverseGeocoderManager;
import com.skobbler.ngx.search.SKSearchResult;
import hk.gavin.navik.core.location.NKLocation;

public class NKSkobblerReverseGeocoder implements NKReverseGeocoder {

    private final SKReverseGeocoderManager mManager;

    public NKSkobblerReverseGeocoder() {
        mManager = SKReverseGeocoderManager.getInstance();
    }

    @Override
    public String getNameFromLocation(NKLocation location) {
        SKSearchResult result = mManager.reverseGeocodePosition(location.toSKCoordinate());
        return result.getName();
    }
}

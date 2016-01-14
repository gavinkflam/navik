package hk.gavin.navik.core.geocode;

import hk.gavin.navik.core.location.NKLocation;

public interface NKReverseGeocoder {

    String getNameFromLocation(NKLocation location);
}

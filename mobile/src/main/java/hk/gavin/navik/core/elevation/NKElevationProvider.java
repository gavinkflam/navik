package hk.gavin.navik.core.elevation;

import hk.gavin.navik.core.location.NKLocation;

import java.util.List;

public abstract class NKElevationProvider {

    public abstract void requestElevation(List<NKLocation> locations);
}

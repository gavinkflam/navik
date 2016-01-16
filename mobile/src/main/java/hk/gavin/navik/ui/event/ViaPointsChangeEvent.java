package hk.gavin.navik.ui.event;

import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.location.NKLocation;

public class ViaPointsChangeEvent {

    public final ImmutableList<NKLocation> viaPoints;

    public ViaPointsChangeEvent(ImmutableList<NKLocation> viaPoints) {
        this.viaPoints = viaPoints;
    }
}

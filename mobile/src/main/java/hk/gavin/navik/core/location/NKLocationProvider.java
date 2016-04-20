package hk.gavin.navik.core.location;

import android.content.Context;
import com.google.common.base.Optional;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.location.event.AccuracyUpdateEvent;
import hk.gavin.navik.core.location.event.LocationUpdateEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class NKLocationProvider {

    @Getter(AccessLevel.PROTECTED) private final Context mContext;
    @Getter private Optional<NKLocation> mLastLocation = Optional.absent();
    @Getter @Setter(AccessLevel.PROTECTED) private double mLastLocationAccuracy;

    public NKLocationProvider(Context context) {
        mContext = context;
    }

    public boolean isLastLocationAvailable() {
        return mLastLocation.isPresent();
    }

    protected void setLastLocation(NKLocation location) {
        mLastLocation = Optional.of(location);
    }

    protected void notifyLocationUpdate() {
        NKBus.get().post(new LocationUpdateEvent(this, mLastLocation.get(), mLastLocationAccuracy));
    }

    protected void notifyAccuracyUpdate() {
        NKBus.get().post(new AccuracyUpdateEvent(this, mLastLocationAccuracy));
    }
}

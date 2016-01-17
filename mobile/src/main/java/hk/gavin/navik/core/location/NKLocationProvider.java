package hk.gavin.navik.core.location;

import android.content.Context;
import com.google.common.base.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "m")
public abstract class NKLocationProvider {

    @Getter(AccessLevel.PROTECTED) private final Context mContext;
    private Optional<NKLocation> mLastLocation = Optional.absent();
    @Getter @Setter(AccessLevel.PROTECTED) private double mLastLocationAccuracy;

    @Getter(AccessLevel.PROTECTED)
    private final List<OnLocationUpdateListener> mOnLocationUpdateListeners = new ArrayList<>();

    public NKLocationProvider(Context context) {
        mContext = context;
    }

    public boolean isLastLocationAvailable() {
        return mLastLocation.isPresent();
    }

    public NKLocation getLastLocation() {
        return mLastLocation.get();
    }

    protected void setLastLocation(NKLocation location) {
        mLastLocation = Optional.of(location);
    }

    protected void notifyLocationUpdate() {
        for (OnLocationUpdateListener listener: mOnLocationUpdateListeners) {
            listener.onLocationUpdated(mLastLocation.get(), mLastLocationAccuracy);
        }
    }

    protected void notifyAccuracyUpdate() {
        for (OnLocationUpdateListener listener: mOnLocationUpdateListeners) {
            listener.onAccuracyUpdated(mLastLocationAccuracy);
        }
    }

    public boolean addPositionUpdateListener(OnLocationUpdateListener listener) {
        return mOnLocationUpdateListeners.add(listener);
    }

    public boolean removePositionUpdateListener(OnLocationUpdateListener listener) {
        return mOnLocationUpdateListeners.remove(listener);
    }

    public interface OnLocationUpdateListener {
        void onLocationUpdated(NKLocation location, double accuracy);
        void onAccuracyUpdated(double accuracy);
    }
}

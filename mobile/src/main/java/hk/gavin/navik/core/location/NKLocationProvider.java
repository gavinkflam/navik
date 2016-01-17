package hk.gavin.navik.core.location;

import android.content.Context;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Accessors(prefix = "m")
public abstract class NKLocationProvider {

    @Getter(AccessLevel.PROTECTED) private Context mContext;
    @Getter @Setter(AccessLevel.PROTECTED) private NKLocation mLastLocation;
    @Getter @Setter(AccessLevel.PROTECTED) private double mLastLocationAccuracy;

    @Getter(AccessLevel.PROTECTED)
    private ArrayList<OnLocationUpdateListener> mOnLocationUpdateListeners = new ArrayList<>();

    public NKLocationProvider(Context context) {
        mContext = context;
    }

    public boolean isLastLocationAvailable() {
        return (mLastLocation != null);
    }

    protected void notifyLocationUpdate() {
        for (OnLocationUpdateListener listener: mOnLocationUpdateListeners) {
            listener.onLocationUpdated(mLastLocation, mLastLocationAccuracy);
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

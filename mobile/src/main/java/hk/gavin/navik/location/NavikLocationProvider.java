package hk.gavin.navik.location;

import android.content.Context;
import com.skobbler.ngx.positioner.SKCurrentPositionListener;
import com.skobbler.ngx.positioner.SKCurrentPositionProvider;
import com.skobbler.ngx.positioner.SKPosition;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Accessors(prefix = "m")
public class NavikLocationProvider implements SKCurrentPositionListener {

    private SKCurrentPositionProvider mProvider;

    @Getter private NavikLocation mLastLocation;
    @Getter private double mLastLocationAccuracy;

    private ArrayList<OnLocationUpdateListener> mListeners = new ArrayList<>();

    public NavikLocationProvider(Context context) {
        mProvider = new SKCurrentPositionProvider(context);
        mProvider.setCurrentPositionListener(this);
    }

    @Override
    public void onCurrentPositionUpdate(SKPosition skPosition) {
        mLastLocation = NavikLocation.fromSKCoordinate(skPosition.getCoordinate());
        mLastLocationAccuracy = skPosition.getHorizontalAccuracy();

        for (OnLocationUpdateListener listener: mListeners) {
            listener.onLocationUpdated(mLastLocation, mLastLocationAccuracy);
        }
    }

    public boolean isLastLocationAvailable() {
        return (mLastLocation != null);
    }

    public boolean addPositionUpdateListener(OnLocationUpdateListener listener) {
        if (mListeners.size() == 0) {
            mProvider.requestLocationUpdates(true, true, true);
        }

        return mListeners.add(listener);
    }

    public boolean removePositionUpdateListener(OnLocationUpdateListener listener) {
        boolean result = mListeners.remove(listener);
        if (mListeners.size() == 0) {
            mProvider.stopLocationUpdates();
        }
        return result;
    }

    public interface OnLocationUpdateListener {
        void onLocationUpdated(NavikLocation location, double accuracy);
    }

}

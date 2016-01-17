package hk.gavin.navik.core.location;

import android.content.Context;
import com.orhanobut.logger.Logger;
import com.skobbler.ngx.positioner.SKCurrentPositionListener;
import com.skobbler.ngx.positioner.SKCurrentPositionProvider;
import com.skobbler.ngx.positioner.SKPosition;

public class NKSkobblerLocationProvider extends NKLocationProvider implements SKCurrentPositionListener {

    private final SKCurrentPositionProvider mProvider;

    public NKSkobblerLocationProvider(Context context) {
        super(context);

        mProvider = new SKCurrentPositionProvider(getContext());
        mProvider.setCurrentPositionListener(this);
    }

    @Override
    public void onCurrentPositionUpdate(SKPosition skPosition) {
        NKLocation newLocation = NKLocation.fromSKCoordinate(skPosition.getCoordinate());
        double newAccuracy = skPosition.getHorizontalAccuracy();
        Logger.d("location: (%s), accuracy: %f", newLocation, newAccuracy);

        if (isLastLocationAvailable() && newLocation.equals(getLastLocation())) {
            if (newAccuracy != getLastLocationAccuracy()) {
                setLastLocationAccuracy(newAccuracy);
                notifyAccuracyUpdate();
            }
        }
        else {
            setLastLocation(NKLocation.fromSKCoordinate(skPosition.getCoordinate()));
            setLastLocationAccuracy(newAccuracy);
            notifyLocationUpdate();
        }
    }

    @Override
    public boolean addPositionUpdateListener(OnLocationUpdateListener listener) {
        if (getOnLocationUpdateListeners().size() == 0) {
            mProvider.requestLocationUpdates(true, true, true);
        }
        return super.addPositionUpdateListener(listener);
    }

    @Override
    public boolean removePositionUpdateListener(OnLocationUpdateListener listener) {
        boolean result = super.removePositionUpdateListener(listener);
        if (getOnLocationUpdateListeners().size() == 0) {
            mProvider.stopLocationUpdates();
        }
        return result;
    }
}

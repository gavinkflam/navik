package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.orhanobut.logger.Logger;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.location.NKLocation;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "m")
public class NKInteractiveDirectionsProvider {

    protected final NKDirectionsProvider mProvider;

    protected Optional<NKLocation> mStartingPoint = Optional.absent();
    protected Optional<NKLocation> mDestination = Optional.absent();
    protected List<NKLocation> mWaypoints = new ArrayList<>();
    @Setter protected int mNoOfDirections = 1;
    @Setter protected boolean mIsManualUpdate = false;

    private ArrayList<DirectionsResultsListener> mListeners = new ArrayList<>();
    private DirectionsResultsCallback mDirectionsResultsCallback = new DirectionsResultsCallback();

    public NKInteractiveDirectionsProvider(NKDirectionsProvider provider) {
        mProvider = provider;
    }

    public void setStartingPoint(NKLocation startingPoint) {
        mStartingPoint = Optional.of(startingPoint);
    }

    public void setDestination(NKLocation destination) {
        mDestination = Optional.of(destination);
    }

    public void removeStartingPoint() {
        mStartingPoint = Optional.absent();
    }

    public void removeDestination() {
        mDestination = Optional.absent();
    }

    public void addWaypoints(NKLocation viaPoint) {
        mWaypoints.add(viaPoint);
    }

    public ImmutableList<NKLocation> getWaypoints() {
        return ImmutableList.copyOf(mWaypoints);
    }

    public int getNoOfWaypoints() {
        return mWaypoints.size();
    }

    public void clearwaypoints() {
        mWaypoints = new ArrayList<>();
    }

    public boolean addDirectionsResultsListener(DirectionsResultsListener listener) {
        return mListeners.add(listener);
    }

    public boolean removeDirectionsResultsListener(DirectionsResultsListener listener) {
        return mListeners.remove(listener);
    }

    public void getCyclingDirections() {
        if (mStartingPoint.isPresent() && mDestination.isPresent()) {
            mProvider
                    .getCyclingDirections(
                            mNoOfDirections, mStartingPoint.get(), mDestination.get(),
                            Optional.of(ImmutableList.copyOf(mWaypoints))
                    )
                    .done(mDirectionsResultsCallback)
                    .fail(mDirectionsResultsCallback);
        }
    }

    private class DirectionsResultsCallback implements
            DoneCallback<ImmutableList<NKDirections>>, FailCallback<NKDirectionsException> {

        @Override
        public void onDone(ImmutableList<NKDirections> result) {
            Logger.d("result size: %d", result.size());
            for (DirectionsResultsListener listener : mListeners) {
                listener.onDirectionsAvailable(result, mIsManualUpdate);
            }
        }

        @Override
        public void onFail(NKDirectionsException result) {
            Logger.d("result: %s", result);
            for (DirectionsResultsListener listener : mListeners) {
                listener.onDirectionsError(result, mIsManualUpdate);
            }
        }
    }

    public interface DirectionsResultsListener {

        void onDirectionsAvailable(ImmutableList<NKDirections> directionsList, boolean isManualUpdate);
        void onDirectionsError(NKDirectionsException exception, boolean isManualUpdate);
    }
}

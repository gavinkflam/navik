package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.location.NKLocation;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;

@Accessors(prefix = "m")
public class NKInteractiveDirectionsProvider {

    protected NKDirectionsProvider mProvider;

    protected Optional<NKLocation> mStartingPoint = Optional.absent();
    protected Optional<NKLocation> mDestination = Optional.absent();
    protected Optional<ImmutableList<NKLocation>> mViaPoints = Optional.absent();
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

    public void setViaPoints(ImmutableList<NKLocation> viaPoints) {
        mViaPoints = Optional.of(viaPoints);
    }

    public void removeStartingPoint() {
        mStartingPoint = Optional.absent();
    }

    public void removeDestination() {
        mDestination = Optional.absent();
    }

    public void removeViaPoints() {
        mViaPoints = Optional.absent();
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
                    .getCyclingDirections(mNoOfDirections, mStartingPoint.get(), mDestination.get(), mViaPoints)
                    .done(mDirectionsResultsCallback)
                    .fail(mDirectionsResultsCallback);
        }
    }

    private class DirectionsResultsCallback implements
            DoneCallback<ImmutableList<NKDirections>>, FailCallback<NKDirectionsException> {

        @Override
        public void onDone(ImmutableList<NKDirections> result) {
            for (DirectionsResultsListener listener : mListeners) {
                listener.onDirectionsAvailable(result, mIsManualUpdate);
            }
        }

        @Override
        public void onFail(NKDirectionsException result) {
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

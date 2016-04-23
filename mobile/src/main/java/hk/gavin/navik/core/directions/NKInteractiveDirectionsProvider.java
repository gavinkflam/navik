package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.orhanobut.logger.Logger;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.directions.contract.DirectionsType;
import hk.gavin.navik.core.directions.event.DestinationChangeEvent;
import hk.gavin.navik.core.directions.event.DirectionsAvailableEvent;
import hk.gavin.navik.core.directions.event.StartingPointChangeEvent;
import hk.gavin.navik.core.directions.event.WaypointsChangeEvent;
import hk.gavin.navik.core.directions.exception.NKDirectionsException;
import hk.gavin.navik.core.location.NKLocation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "m")
public class NKInteractiveDirectionsProvider {

    protected final NKDirectionsProvider mProvider;

    @Getter protected Optional<NKLocation> mStartingPoint = Optional.absent();
    @Getter protected Optional<NKLocation> mDestination = Optional.absent();
    protected List<NKLocation> mWaypoints = new ArrayList<>();
    @Setter protected int mNoOfDirections = 1;

    private DirectionsResultsCallback mOrdinaryDirectionsResultsCallback =
            new DirectionsResultsCallback(DirectionsType.Ordinary);
    private DirectionsResultsCallback mExternalFileDirectionsResultsCallback =
            new DirectionsResultsCallback(DirectionsType.ExternalFile);

    public NKInteractiveDirectionsProvider(NKDirectionsProvider provider) {
        mProvider = provider;
    }

    public void setStartingPoint(NKLocation startingPoint) {
        mStartingPoint = Optional.of(startingPoint);
        notifyStartingPointChange();
    }

    public void setDestination(NKLocation destination) {
        mDestination = Optional.of(destination);
        notifyDestinationChange();
    }

    public void removeStartingPoint() {
        mStartingPoint = Optional.absent();
        notifyStartingPointChange();
    }

    public void removeDestination() {
        mDestination = Optional.absent();
        notifyDestinationChange();
    }

    public void addWaypoints(NKLocation viaPoint) {
        mWaypoints.add(viaPoint);
        notifyWaypointsChange();
    }

    public void clearWaypoints() {
        mWaypoints.clear();
        notifyWaypointsChange();
    }

    public void removeWaypoint(int index) {
        mWaypoints.remove(index);
        notifyWaypointsChange();
    }

    public ImmutableList<NKLocation> getWaypoints() {
        return ImmutableList.copyOf(mWaypoints);
    }

    public int getNoOfWaypoints() {
        return mWaypoints.size();
    }

    public void getCyclingDirections() {
        if (mStartingPoint.isPresent() && mDestination.isPresent()) {
            mProvider
                    .getCyclingDirections(
                            mNoOfDirections, mStartingPoint.get(), mDestination.get(),
                            Optional.of(ImmutableList.copyOf(mWaypoints))
                    )
                    .done(mOrdinaryDirectionsResultsCallback)
                    .fail(mOrdinaryDirectionsResultsCallback);
        }
    }

    public void getCyclingDirectionsFromGpxFile(String gpxPath) {
        mProvider
                .getCyclingDirectionsFromGpxFile(gpxPath)
                .done(mExternalFileDirectionsResultsCallback)
                .fail(mExternalFileDirectionsResultsCallback);
    }

    private void notifyStartingPointChange() {
        NKBus.get().post(new StartingPointChangeEvent(mStartingPoint));
    }

    private void notifyDestinationChange() {
        NKBus.get().post(new DestinationChangeEvent(mStartingPoint));
    }

    private void notifyWaypointsChange() {
        NKBus.get().post(new WaypointsChangeEvent(mWaypoints));
    }

    private class DirectionsResultsCallback implements
            DoneCallback<ImmutableList<NKDirections>>, FailCallback<NKDirectionsException> {

        private DirectionsType mDirectionsType;

        public DirectionsResultsCallback(DirectionsType directionsType) {
            mDirectionsType = directionsType;
        }

        @Override
        public void onDone(ImmutableList<NKDirections> result) {
            if (mDirectionsType == DirectionsType.ExternalFile) {
                NKDirections directions = result.get(0);
                setStartingPoint(directions.startingPoint);
                setDestination(directions.destination);
                clearWaypoints();
            }

            Logger.d("result size: %d", result.size());
            NKBus.get().post(new DirectionsAvailableEvent(result, mDirectionsType));
        }

        @Override
        public void onFail(NKDirectionsException result) {
            Logger.d("result: %s", result);
            NKBus.get().post(result);
        }
    }
}

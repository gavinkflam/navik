package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.orhanobut.logger.Logger;
import com.skobbler.ngx.routing.*;
import hk.gavin.navik.core.directions.exception.NKNoRoutesAvailableException;
import hk.gavin.navik.core.directions.exception.NKUnknownDirectionsException;
import hk.gavin.navik.core.directions.promise.NKDirectionsDeferredObject;
import hk.gavin.navik.core.directions.promise.NKDirectionsPromise;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationUtil;

import java.util.ArrayList;
import java.util.List;

public class NKSkobblerDirectionsProvider implements NKDirectionsProvider {

    @Override
    public NKDirectionsPromise getCyclingDirections(
            int noOfDirections, NKLocation startingPoint, NKLocation destination,
            Optional<ImmutableList<NKLocation>> viaPoints) {
        Logger.d(
                "noOfDirections: %d, startingPoint: (%s), destination: (%s), viapoints size: %d",
                noOfDirections, startingPoint, destination, viaPoints.isPresent() ? viaPoints.get().size() : 0
        );

        NKDirectionsDeferredObject deferred = new NKDirectionsDeferredObject();
        SKRouteManager routeManager = SKRouteManager.getInstance();
        SKRouteSettings routeSettings = new SKRouteSettings();
        SKRouteListener routeListener = new NKSkobblerRouteListener(
                routeManager, deferred, startingPoint, destination, viaPoints
        );

        routeSettings.setStartCoordinate(startingPoint.toSKCoordinate());
        routeSettings.setDestinationCoordinate(destination.toSKCoordinate());
        if (viaPoints.isPresent()) {
            routeSettings.setViaPoints(NKLocationUtil.toSKViaPointList(viaPoints.get()));
        }
        routeSettings.setNoOfRoutes(noOfDirections);
        routeSettings.setRouteExposed(true);

        routeSettings.setRouteMode(SKRouteSettings.SKRouteMode.BICYCLE_QUIETEST);
        routeSettings.setAvoidFerries(true);
        routeSettings.setBicycleCarryAvoided(true);
        routeSettings.setBicycleWalkAvoided(true);
        routeSettings.setFilterAlternatives(true);
        routeSettings.setHighWaysAvoided(true);
        routeSettings.setTollRoadsAvoided(true);
        routeSettings.setUseRoadSlopes(true);

        routeManager.setRouteListener(routeListener);
        routeManager.calculateRoute(routeSettings);
        return deferred.promise();
    }

    private class NKSkobblerRouteListener implements SKRouteListener {

        private final SKRouteManager mRouteManager;
        private final NKDirectionsDeferredObject mDeferred;
        private final NKLocation mStartingPoint;
        private final NKLocation mDestination;
        private final Optional<ImmutableList<NKLocation>> mViaPoints;

        private List<NKDirections> mDirectionsList = new ArrayList<>();
        private Optional<SKRoutingErrorCode> mErrorCode = Optional.absent();

        public NKSkobblerRouteListener(SKRouteManager routeManager, NKDirectionsDeferredObject deferred,
                                       NKLocation startingPoint, NKLocation destination,
                                       Optional<ImmutableList<NKLocation>> viaPoints) {
            mRouteManager = routeManager;
            mDeferred = deferred;
            mStartingPoint = startingPoint;
            mDestination = destination;
            mViaPoints = viaPoints;
        }

        @Override
        public void onRouteCalculationCompleted(SKRouteInfo skRouteInfo) {
            int routeId = skRouteInfo.getRouteID();
            Logger.d("routeId: %d, distance: %d", routeId, skRouteInfo.getDistance());

            mRouteManager.saveRouteToCache(routeId);
            mDirectionsList.add(
                    new NKSkobblerDirections(routeId, mStartingPoint, mDestination, mViaPoints)
            );
        }

        @Override
        public void onRouteCalculationFailed(SKRoutingErrorCode skRoutingErrorCode) {
            Logger.d("error: %s", skRoutingErrorCode);
            mErrorCode = Optional.of(skRoutingErrorCode);
        }

        @Override
        public void onAllRoutesCompleted() {
            if (mDeferred.isRejected() || mDeferred.isResolved()) {
                return;
            }

            Logger.d("list size: %d", mDirectionsList.size());
            if (mDirectionsList.size() == 0) {
                mDeferred.reject(
                        mErrorCode.isPresent() ?
                                new NKNoRoutesAvailableException(mErrorCode.get()) :
                                new NKUnknownDirectionsException()
                );
            }
            else {
                mDeferred.resolve(ImmutableList.copyOf(mDirectionsList));
            }
        }

        @Override
        public void onServerLikeRouteCalculationCompleted(SKRouteJsonAnswer skRouteJsonAnswer) {
            // Do nothing
        }

        @Override
        public void onOnlineRouteComputationHanging(int i) {
            // Do nothing
        }
    }
}

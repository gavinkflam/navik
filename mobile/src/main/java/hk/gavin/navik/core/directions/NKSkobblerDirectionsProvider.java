package hk.gavin.navik.core.directions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.orhanobut.logger.Logger;
import com.skobbler.ngx.routing.*;
import com.skobbler.ngx.tracks.SKTrackElement;
import com.skobbler.ngx.tracks.SKTracksFile;
import com.skobbler.ngx.tracks.SKTracksPoint;
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
            boolean offline, int noOfDirections, NKLocation startingPoint, NKLocation destination,
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

        if (offline) {
            routeSettings.setRouteConnectionMode(SKRouteSettings.SKRouteConnectionMode.OFFLINE);
        }

        routeManager.setRouteListener(routeListener);
        routeManager.calculateRoute(routeSettings);
        return deferred.promise();
    }

    @Override
    public NKDirectionsPromise getCyclingDirectionsFromGpxFile(String gpxPath) {
        Logger.d("gpxPath: %s", gpxPath);

        NKDirectionsDeferredObject deferred = new NKDirectionsDeferredObject();
        SKRouteManager routeManager = SKRouteManager.getInstance();

        // Load file
        SKTracksFile gpxFile = SKTracksFile.loadAtPath(gpxPath);
        SKTrackElement trackElement = gpxFile.getRootTrackElement();

        // Get starting point and destination
        List<SKTracksPoint> points = trackElement.getPointsOnTrackElement();
        NKLocation startingPoint = new NKLocation(points.get(0).getLatitude(), points.get(0).getLongitude());
        NKLocation destination = new NKLocation(
                points.get(points.size() - 1).getLatitude(),
                points.get(points.size() - 1).getLongitude()
        );

        SKRouteListener routeListener = new NKSkobblerRouteListener(
                routeManager, deferred, startingPoint, destination, Optional.<ImmutableList<NKLocation>>absent()
        );
        routeManager.setRouteListener(routeListener);
        routeManager.createRouteFromTrackElement(
                trackElement, SKRouteSettings.SKRouteMode.BICYCLE_QUIETEST, true, true, true
        );

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
            mRouteManager.saveRouteToCache(routeId);

            NKDirections directions = new NKSkobblerDirections(
                    routeId, mStartingPoint, mDestination, mViaPoints,
                    NKLocationUtil.toImmutableNKLocationList(mRouteManager.getCoordinatesForRoute(routeId)),
                    skRouteInfo.getDistance()
            );
            Logger.d(
                    "routeId: %d, distance: %d, noOfPoints: %d",
                    routeId, directions.distance, directions.locations.size()
            );

            mDirectionsList.add(directions);
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

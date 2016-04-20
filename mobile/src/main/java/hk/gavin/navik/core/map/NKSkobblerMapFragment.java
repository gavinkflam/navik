package hk.gavin.navik.core.map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import butterknife.Bind;
import butterknife.OnClick;
import com.google.common.eventbus.Subscribe;
import com.orhanobut.logger.Logger;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.*;
import com.skobbler.ngx.routing.SKRouteManager;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKSkobblerDirections;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.location.event.AccuracyUpdateEvent;
import hk.gavin.navik.core.location.event.LocationUpdateEvent;
import hk.gavin.navik.core.map.event.MapLoadCompleteEvent;
import hk.gavin.navik.core.map.event.MapLongPressEvent;
import hk.gavin.navik.core.map.event.MapMarkerClickEvent;
import hk.gavin.navik.preference.MainPreferences;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class NKSkobblerMapFragment extends NKMapFragment implements SKMapSurfaceListener {

    @Inject NKLocationProvider mLocationProvider;
    @Inject MainPreferences mMainPreferences;
    private final SKRouteManager mRouteManager = SKRouteManager.getInstance();

    @Bind(R.id.moveToCurrentLocation) FloatingActionButton mMoveToCurrentLocationButton;
    @Bind(R.id.skMapHolder) @Getter SKMapViewHolder mMapHolder;
    private SKMapSurfaceView mMap;

    @Getter private final int mLayoutResId = R.layout.fragment_navik_map;

    @Override
    public NKLocation getMapCenter() {
        return NKLocation.fromSKCoordinate(mMap.getMapCenter());
    }

    @Override
    @OnClick(R.id.moveToCurrentLocation)
    public void moveToCurrentLocation() {
        if (isMapLoaded() && mLocationProvider.isLastLocationAvailable()) {
            SKCoordinate coordinate = mLocationProvider.getLastLocation().get().toSKCoordinate();

            mMap.setPositionAsCurrent(coordinate, (float) mLocationProvider.getLastLocationAccuracy(), false);
            mMap.centerMapOnPositionSmooth(coordinate, 200);
        }
    }

    @Override
    public void moveToCurrentLocationOnceAvailable() {
        if (isMapLoaded() && mLocationProvider.isLastLocationAvailable()) {
            moveToCurrentLocation();
        }
        else {
            setPendingMoveToCurrentLocation(true);
        }
    }

    @Override
    public void showMoveToCurrentLocationButton() {
        setDisplayMoveToCurrentLocationButton(true);
        updateMoveToCurrentLocationButtonDisplay();
    }

    @Override
    public void hideMoveToCurrentLocationButton() {
        setDisplayMoveToCurrentLocationButton(false);
        updateMoveToCurrentLocationButtonDisplay();
    }

    @Override
    public void showRoute(NKDirections directions, boolean zoom) {
        if (directions instanceof NKSkobblerDirections) {
            int cacheId = ((NKSkobblerDirections) directions).cacheId;
            Logger.d("directions id: %d", cacheId);

            mRouteManager.loadRouteFromCache(cacheId);
            if (zoom) {
                zoomToCurrentRoute();
            }
        }
    }

    @Override
    public void zoomToCurrentRoute() {
        mRouteManager.zoomMapToCurrentRoute();
    }

    @Override
    public void clearCurrentRoute() {
        mRouteManager.clearCurrentRoute();
    }

    private int annotationTypeOf(MarkerIcon color) {
        switch (color) {
            case Red:
                return SKAnnotation.SK_ANNOTATION_TYPE_RED;
            case Green:
                return SKAnnotation.SK_ANNOTATION_TYPE_GREEN;
            case Blue:
                return SKAnnotation.SK_ANNOTATION_TYPE_BLUE;
            case Flag:
                return SKAnnotation.SK_ANNOTATION_TYPE_DESTINATION_FLAG;
        }
        return SKAnnotation.SK_ANNOTATION_TYPE_MARKER;
    }

    private SKAnnotation annotationOf(int id, NKLocation location, MarkerIcon icon) {
        SKAnnotation annotation = new SKAnnotation(id);
        annotation.setLocation(location.toSKCoordinate());
        annotation.setAnnotationType(annotationTypeOf(icon));
        return annotation;
    }

    @Override
    public void addMarker(int id, NKLocation location, MarkerIcon icon) {
        if (isMapLoaded()) {
            mMap.addAnnotation(annotationOf(id, location, icon), SKAnimationSettings.ANIMATION_PIN_DROP);
        }
    }

    @Override
    public void removeMarker(int id) {
        if (isMapLoaded()) {
            mMap.deleteAnnotation(id);
        }
    }

    @Override
    public void clearMarkers() {
        if (isMapLoaded()) {
            mMap.deleteAllAnnotationsAndCustomPOIs();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapHolder.setMapSurfaceListener(this);
        updateMoveToCurrentLocationButtonDisplay();
    }

    @Override
    public void onPause() {
        NKBus.get().unregister(this);
        mMapHolder.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapHolder.onResume();
        NKBus.get().register(this);
    }

    @Subscribe
    public void onLocationUpdated(LocationUpdateEvent event) {
        if (isMapLoaded()) {
            mMap.setPositionAsCurrent(
                    event.location.toSKCoordinate(), (float) event.accuracy, isPendingMoveToCurrentLocation()
            );
            setPendingMoveToCurrentLocation(false);
        }
    }

    @Subscribe
    public void onAccuracyUpdated(AccuracyUpdateEvent event) {
        if (mLocationProvider.isLastLocationAvailable()) {
            onLocationUpdated(
                    new LocationUpdateEvent(event.provider, mLocationProvider.getLastLocation().get(), event.accuracy)
            );
        }
    }

    @Override
    public void onActionPan() {
        // Do nothing
    }

    @Override
    public void onActionZoom() {
        // Do nothing
    }

    @Override
    public void onSurfaceCreated(SKMapViewHolder skMapViewHolder) {
        if (isMapLoaded()) {
            return;
        }

        setMapLoaded(true);
        mMap = mMapHolder.getMapSurfaceView();
        mMap.centerMapOnPosition(mMainPreferences.getLastLocation().toSKCoordinate());
        mMap.getMapSettings().setShowBicycleLanes(true);
        mMap.getMapSettings().setCurrentPositionShown(true);
        mMap.getMapSettings().setCompassPosition(new SKScreenPoint(-50, -50));

        // Trigger location update immediately if applicable
        if (mLocationProvider.isLastLocationAvailable()) {
            onLocationUpdated(
                    new LocationUpdateEvent(
                            mLocationProvider,
                            mLocationProvider.getLastLocation().get(),
                            mLocationProvider.getLastLocationAccuracy()
                    )
            );
        }

        NKBus.get().post(new MapLoadCompleteEvent(this));
    }

    @Override
    public void onMapRegionChanged(SKCoordinateRegion skCoordinateRegion) {
        // Do nothing
    }

    @Override
    public void onMapRegionChangeStarted(SKCoordinateRegion skCoordinateRegion) {
        // Do nothing
    }

    @Override
    public void onMapRegionChangeEnded(SKCoordinateRegion skCoordinateRegion) {
        // Do nothing
    }

    @Override
    public void onDoubleTap(SKScreenPoint skScreenPoint) {
        // Do nothing
    }

    @Override
    public void onSingleTap(SKScreenPoint skScreenPoint) {
        // Do nothing
    }

    @Override
    public void onRotateMap() {
        if (!mMap.getMapSettings().isCompassShown()) {
            mMap.getMapSettings().setCompassShown(true);
        }
    }

    @Override
    public void onLongPress(SKScreenPoint skScreenPoint) {
        NKBus.get().post(
                new MapLongPressEvent(
                        this, NKLocation.fromSKCoordinate(mMap.pointToCoordinate(skScreenPoint))
                )
        );
    }

    @Override
    public void onInternetConnectionNeeded() {
        // Do nothing
    }

    @Override
    public void onMapActionDown(SKScreenPoint skScreenPoint) {
        // Do nothing
    }

    @Override
    public void onMapActionUp(SKScreenPoint skScreenPoint) {
        // Do nothing
    }

    @Override
    public void onPOIClusterSelected(SKPOICluster skpoiCluster) {
        // Do nothing
    }

    @Override
    public void onMapPOISelected(SKMapPOI skMapPOI) {
        // Do nothing
    }

    @Override
    public void onAnnotationSelected(SKAnnotation skAnnotation) {
        NKBus.get().post(
                new MapMarkerClickEvent(
                        this, skAnnotation.getUniqueID(), NKLocation.fromSKCoordinate(skAnnotation.getLocation())
                )
        );
    }

    @Override
    public void onCustomPOISelected(SKMapCustomPOI skMapCustomPOI) {
        // Do nothing
    }

    @Override
    public void onCompassSelected() {
        mMap.rotateTheMapToNorthSmooth(200);
        if (mMap.getMapSettings().isCompassShown()) {
            mMap.getMapSettings().setCompassShown(false);
        }
    }

    @Override
    public void onCurrentPositionSelected() {
        // Do nothing
    }

    @Override
    public void onObjectSelected(int i) {
        // Do nothing
    }

    @Override
    public void onInternationalisationCalled(int i) {
        // Do nothing
    }

    @Override
    public void onBoundingBoxImageRendered(int i) {
        // Do nothing
    }

    @Override
    public void onGLInitializationError(String s) {
        // Do nothing
    }

    @Override
    public void onScreenshotReady(Bitmap bitmap) {
        // Do nothing
    }

    private void updateMoveToCurrentLocationButtonDisplay() {
        if (isDisplayMoveToCurrentLocationButton()) {
            mMoveToCurrentLocationButton.show();
        }
        else {
            mMoveToCurrentLocationButton.hide();
        }
    }
}

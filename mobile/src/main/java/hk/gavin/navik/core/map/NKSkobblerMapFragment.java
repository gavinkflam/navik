package hk.gavin.navik.core.map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import butterknife.Bind;
import butterknife.OnClick;
import com.google.common.base.Optional;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.*;
import com.skobbler.ngx.navigation.SKNavigationSettings;
import com.skobbler.ngx.routing.SKRouteManager;
import com.skobbler.ngx.routing.SKRouteSettings;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationConfiguration;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationListener;
import com.skobbler.ngx.sdktools.navigationui.SKToolsNavigationManager;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKSkobblerDirections;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.location.NKSkobblerLocationProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class NKSkobblerMapFragment extends NKMapFragment
        implements SKMapSurfaceListener, NKLocationProvider.OnLocationUpdateListener, SKToolsNavigationListener {

    private NKLocationProvider mLocationProvider;
    private final SKRouteManager mRouteManager = SKRouteManager.getInstance();
    private Optional<SKToolsNavigationManager> mNavigationManager = Optional.absent();

    @Bind(R.id.moveToCurrentLocation) FloatingActionButton mMoveToCurrentLocationButton;
    @Bind(R.id.skMapHolder) SKMapViewHolder mMapHolder;
    private SKMapSurfaceView mMap;

    @Getter(AccessLevel.PROTECTED) private boolean mActivityCreated = false;

    @Override
    public NKLocation getMapCenter() {
        return NKLocation.fromSKCoordinate(mMap.getMapCenter());
    }

    @Override
    @OnClick(R.id.moveToCurrentLocation)
    public void moveToCurrentLocation() {
        if (isMapLoaded() && isActivityCreated()  && mLocationProvider.isLastLocationAvailable()) {
            SKCoordinate coordinate = mLocationProvider.getLastLocation().toSKCoordinate();

            mMap.setPositionAsCurrent(coordinate, (float) mLocationProvider.getLastLocationAccuracy(), false);
            mMap.centerMapOnPositionSmooth(coordinate, 200);
        }
    }

    @Override
    public void moveToCurrentLocationOnceAvailable() {
        if (isActivityCreated() && mLocationProvider.isLastLocationAvailable()) {
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

    @Override
    public void startNavigation() {
        if (!mNavigationManager.isPresent()) {
            mNavigationManager = Optional.of(
                    new SKToolsNavigationManager(getActivity(), R.id.nkSKMapContainer)
            );
        }

        SKToolsNavigationConfiguration configuration = new SKToolsNavigationConfiguration();
        configuration.setNavigationType(SKNavigationSettings.SKNavigationType.SIMULATION);
        configuration.setRouteType(SKRouteSettings.SKRouteMode.BICYCLE_QUIETEST);

        mNavigationManager.get().setNavigationListener(this);
        mNavigationManager.get().startNavigation(configuration, mMapHolder);
    }

    @Override
    public void stopNavigation() {
        mNavigationManager.get().stopNavigation();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivityCreated = true;
        mLocationProvider = new NKSkobblerLocationProvider(getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapHolder.setMapSurfaceListener(this);
        updateMoveToCurrentLocationButtonDisplay();
    }

    @Override
    public void onPause() {
        mMapHolder.onPause();
        mLocationProvider.removePositionUpdateListener(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationProvider.addPositionUpdateListener(this);
        mMapHolder.onResume();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_navik_map;
    }

    @Override
    public void onLocationUpdated(NKLocation location, double accuracy) {
        if (isMapLoaded()) {
            mMap.setPositionAsCurrent(
                    location.toSKCoordinate(), (float) accuracy, isPendingMoveToCurrentLocation()
            );
            setPendingMoveToCurrentLocation(false);
        }
    }

    @Override
    public void onAccuracyUpdated(double accuracy) {
        if (mLocationProvider.isLastLocationAvailable()) {
            onLocationUpdated(mLocationProvider.getLastLocation(), accuracy);
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
        mMap.getMapSettings().setShowBicycleLanes(true);
        mMap.getMapSettings().setCurrentPositionShown(true);
        mMap.getMapSettings().setCompassPosition(new SKScreenPoint(-50, -50));
        mLocationProvider.addPositionUpdateListener(this);

        // Trigger location update immediately if applicable
        if (mLocationProvider.isLastLocationAvailable()) {
            onLocationUpdated(mLocationProvider.getLastLocation(), mLocationProvider.getLastLocationAccuracy());
        }

        if (getMapEventsListener().isPresent()) {
            getMapEventsListener().get().onMapLoadComplete();
        }
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
        // Do nothing
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
        // Do nothing
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

    @Override
    public void onNavigationStarted() {
        // Do nothing
    }

    @Override
    public void onNavigationEnded() {
        // Do nothing
    }

    @Override
    public void onRouteCalculationStarted() {
        // Do nothing
    }

    @Override
    public void onRouteCalculationCompleted() {
        // Do nothing
    }

    @Override
    public void onRouteCalculationCanceled() {
        // Do nothing
    }

    private void updateMoveToCurrentLocationButtonDisplay() {
        if (isViewInjected()) {
            if (isDisplayMoveToCurrentLocationButton()) {
                mMoveToCurrentLocationButton.show();
            }
            else {
                mMoveToCurrentLocationButton.hide();
            }
        }
    }
}

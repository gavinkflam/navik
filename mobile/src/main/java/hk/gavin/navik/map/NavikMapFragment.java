package hk.gavin.navik.map;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.*;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.AbstractNavikActivity;
import hk.gavin.navik.location.NavikLocation;
import hk.gavin.navik.location.NavikLocationProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class NavikMapFragment extends Fragment
        implements SKMapSurfaceListener, NavikLocationProvider.OnLocationUpdateListener {

    @Inject NavikLocationProvider mLocationProvider;

    @Bind(R.id.mapHolder) @Getter SKMapViewHolder mMapHolder;
    @Bind(R.id.moveToCurrentLocation) FloatingActionButton mMoveToCurrentLocationButton;
    @Getter private SKMapSurfaceView mMap;
    @Setter private MapEventsListener mMapEventsListener;

    private boolean mPendingMoveToCurrentLocation = false;
    private boolean mDisplayMoveToCurrentLocationButton = false;

    static {
        // In order to support multiple maps
        SKMapSurfaceView.preserveGLContext = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AbstractNavikActivity) getActivity()).component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navik_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
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

    public void moveToCurrentLocationOnceAvailable() {
        if (mLocationProvider != null && mLocationProvider.isLastLocationAvailable()) {
            moveToCurrentLocation();
        }
        else {
            mPendingMoveToCurrentLocation = true;
        }
    }

    private void updateMoveToCurrentLocationButtonDisplay() {
        if (mMoveToCurrentLocationButton != null) {
            if (mDisplayMoveToCurrentLocationButton) {
                mMoveToCurrentLocationButton.show();
            }
            else {
                mMoveToCurrentLocationButton.hide();
            }
        }
    }

    public void showMoveToCurrentLocationButton() {
        mDisplayMoveToCurrentLocationButton = true;
        updateMoveToCurrentLocationButtonDisplay();
    }

    public void hideMoveToCurrentLocationButton() {
        mDisplayMoveToCurrentLocationButton = false;
        updateMoveToCurrentLocationButtonDisplay();
    }

    @OnClick(R.id.moveToCurrentLocation)
    public void moveToCurrentLocation() {
        if (mMap != null && mLocationProvider != null && mLocationProvider.isLastLocationAvailable()) {
            SKCoordinate coordinate = mLocationProvider.getLastLocation().toSKCoordinate();

            mMap.setPositionAsCurrent(coordinate, (float) mLocationProvider.getLastLocationAccuracy(), false);
            mMap.centerMapOnPositionSmooth(coordinate, 200);
        }
    }

    @Override
    public void onLocationUpdated(NavikLocation location, double accuracy) {
        if (mMap != null) {
            mMap.setPositionAsCurrent(
                    location.toSKCoordinate(), (float) accuracy, mPendingMoveToCurrentLocation
            );
            mPendingMoveToCurrentLocation = false;
        }
    }

    @Override
    public void onActionPan() {

    }

    @Override
    public void onActionZoom() {

    }

    @Override
    public void onSurfaceCreated(SKMapViewHolder skMapViewHolder) {
        if (mMap != null) {
            return;
        }

        mMap = mMapHolder.getMapSurfaceView();
        mMap.getMapSettings().setShowBicycleLanes(true);
        mMap.getMapSettings().setCurrentPositionShown(true);
        mMap.getMapSettings().setCompassPosition(new SKScreenPoint(-50, -50));
        mLocationProvider.addPositionUpdateListener(this);

        // Trigger location update immediately if applicable
        if (mLocationProvider.isLastLocationAvailable()) {
            onLocationUpdated(mLocationProvider.getLastLocation(), mLocationProvider.getLastLocationAccuracy());
        }

        if (mMapEventsListener != null) {
            mMapEventsListener.onMapLoadComplete();
        }
    }

    @Override
    public void onMapRegionChanged(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeStarted(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeEnded(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onDoubleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onSingleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onRotateMap() {
        if (!mMap.getMapSettings().isCompassShown()) {
            mMap.getMapSettings().setCompassShown(true);
        }
    }

    @Override
    public void onLongPress(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onInternetConnectionNeeded() {

    }

    @Override
    public void onMapActionDown(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onMapActionUp(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onPOIClusterSelected(SKPOICluster skpoiCluster) {

    }

    @Override
    public void onMapPOISelected(SKMapPOI skMapPOI) {

    }

    @Override
    public void onAnnotationSelected(SKAnnotation skAnnotation) {

    }

    @Override
    public void onCustomPOISelected(SKMapCustomPOI skMapCustomPOI) {

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

    }

    @Override
    public void onObjectSelected(int i) {

    }

    @Override
    public void onInternationalisationCalled(int i) {

    }

    @Override
    public void onBoundingBoxImageRendered(int i) {

    }

    @Override
    public void onGLInitializationError(String s) {

    }

    @Override
    public void onScreenshotReady(Bitmap bitmap) {

    }

    public interface MapEventsListener {
        void onMapLoadComplete();
    }
}

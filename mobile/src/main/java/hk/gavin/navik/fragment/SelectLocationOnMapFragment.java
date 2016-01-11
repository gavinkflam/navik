package hk.gavin.navik.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.skobbler.ngx.map.*;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.SelectLocationOnMapActivity;

public class SelectLocationOnMapFragment extends Fragment {

    @Bind(R.id.mapHolder) SKMapViewHolder mMapHolder;
    SKMapSurfaceView mMap;
    MapHandler mMapHandler = new MapHandler();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((SelectLocationOnMapActivity) getActivity()).component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_location_on_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mMapHolder.setMapSurfaceListener(mMapHandler);
    }

    @Override
    public void onPause() {
        mMapHolder.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapHolder.onResume();
        super.onResume();
    }

    private class MapHandler implements SKMapSurfaceListener {

        @Override
        public void onActionPan() {

        }

        @Override
        public void onActionZoom() {

        }

        @Override
        public void onSurfaceCreated(SKMapViewHolder skMapViewHolder) {
            mMap = mMapHolder.getMapSurfaceView();
            mMap.getMapSettings().setShowBicycleLanes(true);
            mMap.getMapSettings().setCurrentPositionShown(true);
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

    }

}

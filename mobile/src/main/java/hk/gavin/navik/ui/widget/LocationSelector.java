package hk.gavin.navik.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.contract.UiContract;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class LocationSelector extends FrameLayout implements PopupMenu.OnMenuItemClickListener, NKLocationProvider.OnLocationUpdateListener {

    @Bind(R.id.prefix) TextView mPrefix;
    @Bind(R.id.location_text) TextView mLocationText;
    @Bind(R.id.placeholder) TextView mPlaceholder;
    PopupMenu mPopupMenu;

    @BindString(R.string.current_location) String mCurrentLocationString;

    @Getter boolean mUseCurrentLocation = false;
    Optional<NKLocation> mLocation = Optional.absent();
    Optional<LocationSelectorEventsListener> mLocationSelectorEventsListener = Optional.absent();

    private NKLocationProvider mLocationProvider;
    private NKReverseGeocoder mReverseGeocoder;

    public LocationSelector(Context context) {
        super(context);
        initView(context);
        updateLocationDisplay();
        preparePopupMenu(context);
    }

    public LocationSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        applyStyledAttributes(context, attrs);
        updateLocationDisplay();
        preparePopupMenu(context);
    }

    public LocationSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        applyStyledAttributes(context, attrs);
        preparePopupMenu(context);
    }

    public void updateLocationDisplay() {
        if (!mUseCurrentLocation && !mLocation.isPresent()) {
            mPrefix.setVisibility(GONE);
            mLocationText.setVisibility(GONE);
            mPlaceholder.setVisibility(VISIBLE);
        }
        else {
            mPrefix.setVisibility(VISIBLE);
            mLocationText.setVisibility(VISIBLE);
            mPlaceholder.setVisibility(GONE);
            updateLocationName();
        }
    }

    public void initialize(NKLocationProvider locationProvider, NKReverseGeocoder reverseGeocoder) {
        mLocationProvider = locationProvider;
        mReverseGeocoder = reverseGeocoder;
    }

    public NKLocation getLocation() {
        return mLocation.get();
    }

    public void setLocation(NKLocation location, boolean isManualUpdate) {
        setLocation(Optional.of(location), isManualUpdate);
    }

    public void removeLocation() {
        setLocation(Optional.<NKLocation>absent(), true);
    }

    public boolean isLocationAvailable() {
        return mLocation.isPresent();
    }

    public void useCurrentLocation() {
        mLocation = Optional.absent();
        mUseCurrentLocation = true;
        updateLocationDisplay();

        if (mLocationProvider.isLastLocationAvailable()) {
            mLocation = Optional.of(mLocationProvider.getLastLocation());
            invokeOnLocationUpdatedListener(false);
        }
        mLocationProvider.addPositionUpdateListener(this);
    }

    public void setLocationSelectorEventsListener(LocationSelectorEventsListener listener) {
        mLocationSelectorEventsListener = Optional.of(listener);
    }

    public void removeLocationSelectorEventsListener() {
        mLocationSelectorEventsListener = Optional.absent();
    }

    private void initView(Context context) {
        inflate(context, R.layout.widget_location_selector, this);
        ButterKnife.bind(this);
    }

    private void applyStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.LocationSelector, 0, 0
        );

        try {
            mPrefix.setText(
                    array.getString(R.styleable.LocationSelector_prefix)
            );
            mPlaceholder.setText(
                    array.getString(R.styleable.LocationSelector_placeholder)
            );
        }
        finally {
            array.recycle();
        }
    }

    private void preparePopupMenu(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPopupMenu = new PopupMenu(context, this, Gravity.END);
        }
        else {
            mPopupMenu = new PopupMenu(context, this);
        }
        mPopupMenu.inflate(R.menu.popup_menu_location_selector);
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    private void invokeOnLocationUpdatedListener(boolean isManualUpdate) {
        if (mLocation.isPresent()) {
            if (mLocationSelectorEventsListener.isPresent()) {
                mLocationSelectorEventsListener.get().onLocationUpdated(this, mLocation.get(), isManualUpdate);
            }
        }
    }

    private void setLocation(Optional<NKLocation> location, boolean isManualUpdate) {
        mLocationProvider.removePositionUpdateListener(this);
        mLocation = location;
        mUseCurrentLocation = false;
        updateLocationDisplay();
        invokeOnLocationUpdatedListener(isManualUpdate);
    }

    private void updateLocationName() {
        if (mUseCurrentLocation) {
            mLocationText.setText(mCurrentLocationString);
        }
        else if (mLocation.isPresent()) {
            mLocationText.setText(
                    mReverseGeocoder.getNameFromLocation(mLocation.get())
            );
        }
    }

    @OnClick
    void showPopupMenu() {
        mPopupMenu.show();
    }

    @Override
    public void onLocationUpdated(NKLocation location, double accuracy) {
        if (mUseCurrentLocation) {
            mLocation = Optional.of(location);
            invokeOnLocationUpdatedListener(false);
        }
    }

    @Override
    public void onAccuracyUpdated(double accuracy) {
        // Do nothing
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.current_location: {
                useCurrentLocation();
                return true;
            }
//            case R.id.history: {
//                if (mLocationSelectorEventsListener.isPresent()) {
//                    mLocationSelectorEventsListener.get().onHistoryClicked(this);
//                }
//                return true;
//            }
            case R.id.select_on_map: {
                if (mLocationSelectorEventsListener.isPresent()) {
                    mLocationSelectorEventsListener.get().onSelectLocationOnMapClicked(this);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(UiContract.DataKey.SUPER_STATE, super.onSaveInstanceState());
        bundle.putBoolean(UiContract.DataKey.USE_CURRENT_LOCATION, mUseCurrentLocation);
        if (mLocation.isPresent()) {
            bundle.putSerializable(UiContract.DataKey.LOCATION, mLocation.get());
        }
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mUseCurrentLocation = bundle.getBoolean(UiContract.DataKey.USE_CURRENT_LOCATION);
            mLocation = Optional.fromNullable(
                    (NKLocation) bundle.getSerializable(UiContract.DataKey.LOCATION)
            );
            updateLocationDisplay();
            state = bundle.getParcelable(UiContract.DataKey.SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    public interface LocationSelectorEventsListener {
        void onLocationUpdated(LocationSelector selector, NKLocation location, boolean isManualUpdate);
        void onHistoryClicked(LocationSelector selector);
        void onSelectLocationOnMapClicked(LocationSelector selector);
    }
}

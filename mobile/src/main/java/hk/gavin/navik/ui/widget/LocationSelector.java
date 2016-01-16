package hk.gavin.navik.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
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
import hk.gavin.navik.R;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class LocationSelector extends FrameLayout implements PopupMenu.OnMenuItemClickListener, NKLocationProvider.OnLocationUpdateListener {

    @Bind(R.id.prefix) TextView mPrefix;
    @Bind(R.id.location_text) TextView mLocationText;
    @Bind(R.id.placeholder) TextView mPlaceholder;
    PopupMenu mPopupMenu;

    @BindString(R.string.current_location) String mCurrentLocationString;

    @Getter boolean mUseCurrentLocation = false;
    @Getter NKLocation mLocation;
    @Getter @Setter String mLocationName;
    @Setter OnLocationUpdatedListener mOnLocationUpdatedListener;
    @Setter OnMenuItemClickListener mOnMenuItemClickListener;

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

    public void initialize(NKLocationProvider locationProvider, NKReverseGeocoder reverseGeocoder) {
        mLocationProvider = locationProvider;
        mReverseGeocoder = reverseGeocoder;
    }

    public void setLocation(NKLocation location) {
        mLocation = location;
        mUseCurrentLocation = false;
        mLocationProvider.removePositionUpdateListener(this);
        updateLocationDisplay();
        invokeOnLocationUpdatedListener();
    }

    public boolean isLocationAvailable() {
        return (mLocation != null);
    }

    public void useCurrentLocation() {
        mLocation = null;
        mUseCurrentLocation = true;
        mLocationProvider.addPositionUpdateListener(this);
        updateLocationDisplay();

        if (mLocationProvider.isLastLocationAvailable()) {
            mLocation = mLocationProvider.getLastLocation();
            invokeOnLocationUpdatedListener();
        }
    }

    private void invokeOnLocationUpdatedListener() {
        if (mOnLocationUpdatedListener != null) {
            mOnLocationUpdatedListener.onLocationUpdated(this, mLocation);
        }
    }

    private void updateLocationName() {
        if (mUseCurrentLocation) {
            mLocationText.setText(mCurrentLocationString);
        }
        else if (mReverseGeocoder != null) {
            mLocationText.setText(
                    mReverseGeocoder.getNameFromLocation(mLocation)
            );
        }
    }

    public void updateLocationDisplay() {
        if (!mUseCurrentLocation && mLocation == null) {
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

    @OnClick
    void showPopupMenu() {
        mPopupMenu.show();
    }

    @Override
    public void onLocationUpdated(NKLocation location, double accuracy) {
        if (mUseCurrentLocation) {
            mLocation = location;
            invokeOnLocationUpdatedListener();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.current_location: {
                if (mOnMenuItemClickListener != null) {
                    mOnMenuItemClickListener.onCurrentLocationClicked(this);
                }
                return true;
            }
//            case R.id.history: {
//                if (mOnMenuItemClickListener != null) {
//                    mOnMenuItemClickListener.onHistoryClicked();
//                }
//                return true;
//            }
            case R.id.select_on_map: {
                if (mOnMenuItemClickListener != null) {
                    mOnMenuItemClickListener.onSelectLocationOnMapClicked(this);
                }
                return true;
            }
        }
        return false;
    }

    public interface OnLocationUpdatedListener {
        void onLocationUpdated(LocationSelector selector, NKLocation location);
    }

    public interface OnMenuItemClickListener {
        void onCurrentLocationClicked(LocationSelector selector);
        void onHistoryClicked(LocationSelector selector);
        void onSelectLocationOnMapClicked(LocationSelector selector);
    }

}

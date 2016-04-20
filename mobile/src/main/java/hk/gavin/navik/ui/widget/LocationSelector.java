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
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.ui.widget.event.LocationSelectionChangeEvent;
import hk.gavin.navik.ui.widget.event.SelectAsStartingPointEvent;
import hk.gavin.navik.ui.widget.event.SelectCurrentLocationEvent;
import hk.gavin.navik.ui.widget.event.SelectLocationOnMapEvent;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class LocationSelector extends FrameLayout implements PopupMenu.OnMenuItemClickListener {

    @Bind(R.id.prefix) TextView mPrefix;
    @Bind(R.id.location_text) TextView mLocationText;
    @Bind(R.id.placeholder) TextView mPlaceholder;
    private PopupMenu mPopupMenu;
    private int mMenuRes = R.menu.popup_menu_location_selector;

    @Getter Optional<NKLocation> mLocation = Optional.absent();

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
        if (mLocation.isPresent()) {
            mPrefix.setVisibility(VISIBLE);
            mLocationText.setVisibility(VISIBLE);
            mPlaceholder.setVisibility(GONE);
            updateLocationName();
        }
        else {
            mPrefix.setVisibility(GONE);
            mLocationText.setVisibility(GONE);
            mPlaceholder.setVisibility(VISIBLE);
        }
    }

    public void initialize(NKReverseGeocoder reverseGeocoder) {
        mReverseGeocoder = reverseGeocoder;
    }

    public void removeLocation() {
        setLocation(Optional.<NKLocation>absent());
    }

    private void initView(Context context) {
        inflate(context, R.layout.widget_location_selector, this);
        ButterKnife.bind(this);
    }

    private void applyStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.LocationSelector, 0, 0
        );

        if (array.hasValue(R.styleable.LocationSelector_prefix)) {
            mPrefix.setText(
                    array.getString(R.styleable.LocationSelector_prefix)
            );
        }
        if (array.hasValue(R.styleable.LocationSelector_placeholder)) {
            mPlaceholder.setText(
                    array.getString(R.styleable.LocationSelector_placeholder)
            );
        }
        if (array.hasValue(R.styleable.LocationSelector_menu_resource)) {
            mMenuRes = array.getResourceId(R.styleable.LocationSelector_menu_resource, 0);
        }

        array.recycle();
    }

    private void preparePopupMenu(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPopupMenu = new PopupMenu(context, this, Gravity.END);
        } else {
            mPopupMenu = new PopupMenu(context, this);
        }

        mPopupMenu.inflate(mMenuRes);
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    public void setLocation(NKLocation location) {
        setLocation(Optional.of(location));
    }

    private void setLocation(Optional<NKLocation> location) {
        mLocation = location;
        updateLocationDisplay();
        NKBus.get().post(new LocationSelectionChangeEvent(this, mLocation));
    }

    private void updateLocationName() {
        if (mLocation.isPresent()) {
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
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.current_location: {
                NKBus.get().post(new SelectCurrentLocationEvent(this));
                return true;
            }
            case R.id.as_starting_point: {
                NKBus.get().post(new SelectAsStartingPointEvent(this));
                return true;
            }
            case R.id.select_on_map: {
                NKBus.get().post(new SelectLocationOnMapEvent(this));
                return true;
            }
        }
        return false;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(UiContract.DataKey.SUPER_STATE, super.onSaveInstanceState());
        if (mLocation.isPresent()) {
            bundle.putSerializable(UiContract.DataKey.LOCATION, mLocation.get());
        }
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mLocation = Optional.fromNullable(
                    (NKLocation) bundle.getSerializable(UiContract.DataKey.LOCATION)
            );
            updateLocationDisplay();
            state = bundle.getParcelable(UiContract.DataKey.SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }
}

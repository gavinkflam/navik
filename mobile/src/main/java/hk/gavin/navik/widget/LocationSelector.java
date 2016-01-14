package hk.gavin.navik.widget;

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
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.core.location.NKLocation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class LocationSelector extends FrameLayout implements PopupMenu.OnMenuItemClickListener {

    @Bind(R.id.prefix) TextView mPrefix;
    @Bind(R.id.location_text) TextView mLocationText;
    @Bind(R.id.placeholder) TextView mPlaceholder;
    PopupMenu mPopupMenu;

    @Getter
    NKLocation mLocation;
    @Setter OnLocationUpdatedListener mOnLocationUpdatedListener;
    @Setter OnMenuItemClickListener mOnMenuItemClickListener;

    public LocationSelector(Context context) {
        super(context);
        initView(context);
        invalidateLocationDisplay();
        preparePopupMenu(context);
    }

    public LocationSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        applyStyledAttributes(context, attrs);
        invalidateLocationDisplay();
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

    public void setLocation(NKLocation location) {
        mLocation = location;
        invalidateLocationDisplay();

        if (mOnLocationUpdatedListener != null) {
            mOnLocationUpdatedListener.onLocationUpdated(mLocation);
        }
    }

    public void invalidateLocationDisplay() {
        if (mLocation == null) {
            mPrefix.setVisibility(GONE);
            mLocationText.setVisibility(GONE);
            mPlaceholder.setVisibility(VISIBLE);
        }
        else {
            mPrefix.setVisibility(VISIBLE);
            mLocationText.setVisibility(VISIBLE);
            mPlaceholder.setVisibility(GONE);
            // mLocationText.setText(mLocation.getName());
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
                if (mOnMenuItemClickListener != null) {
                    mOnMenuItemClickListener.onCurrentLocationClicked();
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
                    mOnMenuItemClickListener.onSelectLocationOnMapClicked();
                }
                return true;
            }
        }
        return false;
    }

    public interface OnLocationUpdatedListener {
        void onLocationUpdated(NKLocation location);
    }

    public interface OnMenuItemClickListener {
        void onCurrentLocationClicked();
        void onHistoryClicked();
        void onSelectLocationOnMapClicked();
    }

}

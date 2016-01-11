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
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.location.NavikLocation;
import lombok.Getter;
import lombok.Setter;

public class LocationSelector extends FrameLayout implements PopupMenu.OnMenuItemClickListener {

    @Bind(R.id.prefix) TextView mPrefix;
    @Bind(R.id.location_text) TextView mLocationText;
    @Bind(R.id.placeholder) TextView mPlaceholder;
    PopupMenu mPopupMenu;

    @Getter NavikLocation mLocation;
    @Setter LocationUpdatedListener mLocationUpdatedListener;

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

    public void setLocation(NavikLocation location) {
        mLocation = location;
        invalidateLocationDisplay();

        if (mLocationUpdatedListener != null) {
            mLocationUpdatedListener.onLocationUpdated(mLocation);
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
            mLocationText.setText(mLocation.getName());
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
                Toast.makeText(getContext(), "Current Location", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.history: {
                Toast.makeText(getContext(), "History", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.select_on_map: {
                Toast.makeText(getContext(), "Select On Map", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public interface LocationUpdatedListener {
        void onLocationUpdated(NavikLocation location);
    }

}

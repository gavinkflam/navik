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

public class LocationSelector extends FrameLayout implements PopupMenu.OnMenuItemClickListener {

    @Bind(R.id.prefix) TextView mPrefix;
    @Bind(R.id.location_text) TextView mLocationText;
    PopupMenu mPopupMenu;

    public LocationSelector(Context context) {
        super(context);
        initView(context);
        preparePopupMenu(context);
    }

    public LocationSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        applyStyledAttributes(context, attrs);
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
}

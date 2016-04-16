package hk.gavin.navik.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import butterknife.BindColor;
import butterknife.ButterKnife;
import hk.gavin.navik.R;

public class TwoStatedFloatingActionButton extends FloatingActionButton {

    @BindColor(R.color.colorAccent) int mColorEnabled;
    @BindColor(R.color.gray)        int mColorDisabled;

    public TwoStatedFloatingActionButton(Context context) {
        super(context);
        ButterKnife.bind(this);
    }

    public TwoStatedFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(this);
    }

    public TwoStatedFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ButterKnife.bind(this);
    }

    public void enable() {
        setBackgroundTintList(ColorStateList.valueOf(mColorEnabled));
    }

    public void disable() {
        setBackgroundTintList(ColorStateList.valueOf(mColorDisabled));
    }
}

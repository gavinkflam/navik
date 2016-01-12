package hk.gavin.navik;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends WearableActivity {

    @Bind(R.id.container) BoxInsetLayout mContainer;
    @Bind(R.id.visualAdvisor) ImageView mVisualAdvisor;
    @Bind(R.id.metersToTurn) TextView mMetersToTurn;
    @Bind(R.id.streetName) TextView mStreesName;
    @Bind(R.id.progress) TextView mProgress;

    private int mColorMode = 0;
    @BindColor(R.color.colorSafe) int mColorSafe;
    @BindColor(R.color.colorSoon) int mColorSoon;
    @BindColor(R.color.colorImmediate) int mColorImmediate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.visualAdvisor)
    void changeBackgroundColor() {
        switch (mColorMode) {
            case 0: {
                mContainer.setBackgroundColor(mColorSoon);
                mColorMode = 1;
                break;
            }
            case 1: {
                mContainer.setBackgroundColor(mColorImmediate);
                mColorMode = 2;
                break;
            }
            default: {
                mContainer.setBackgroundColor(mColorSafe);
                mColorMode = 0;
            }
        }
    }
}

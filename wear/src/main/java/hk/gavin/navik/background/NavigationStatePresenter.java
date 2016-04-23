package hk.gavin.navik.background;

import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import hk.gavin.navik.R;
import hk.gavin.navik.core.navigation.NKNavigationState;
import hk.gavin.navik.util.FormattingUtility;

public class NavigationStatePresenter {

    @Bind(R.id.splashScreen) LinearLayout mSplashScreen;
    @Bind(R.id.container) BoxInsetLayout mContainer;
    @Bind(R.id.visualAdvice) ImageView mVisualAdvice;
    @Bind(R.id.distanceToNextAdvice) TextView mDistanceToNextAdvice;
    @Bind(R.id.nextStreetName) TextView mNextStreetName;
    @Bind(R.id.distanceToDestination) TextView mDistanceToDestination;
    @Bind(R.id.currentSpeed) TextView mCurrentSpeed;

    @BindColor(R.color.colorSafe) int mColorSafe;
    @BindColor(R.color.colorSoon) int mColorSoon;
    @BindColor(R.color.colorImmediate) int mColorImmediate;

    private NKNavigationState mNavigationState;

    public void setNavigationState(NKNavigationState navigationState) {
        mNavigationState = navigationState;
        invalidate();
    }

    public void invalidate() {
        if (mNavigationState == null) {
            mSplashScreen.setVisibility(View.VISIBLE);
            return;
        }
        mSplashScreen.setVisibility(View.GONE);

        // Display information
        mVisualAdvice.setImageBitmap(mNavigationState.visualAdviceImage.getBitmap());
        mDistanceToNextAdvice.setText(
                FormattingUtility.formatDistanceReadableRounded(mNavigationState.distanceToNextAdvice)
        );
        mNextStreetName.setText(mNavigationState.nextStreetName);

        mDistanceToDestination.setText(
                FormattingUtility.formatDistanceReadableRounded(mNavigationState.distanceToDestination)
        );
        mCurrentSpeed.setText(
                FormattingUtility.formatSpeedReadable(mNavigationState.currentSpeed)
        );

        // Determine background color
        if (mNavigationState.distanceToNextAdvice > 249) {
            // Safe for 249m up
            mContainer.setBackgroundColor(mColorSafe);
        }
        else if (mNavigationState.distanceToNextAdvice > 99) {
            // Soon for 100m - 199m
            mContainer.setBackgroundColor(mColorSoon);
        }
        else {
            // Immediate for under 99m
            mContainer.setBackgroundColor(mColorImmediate);
        }
    }

}

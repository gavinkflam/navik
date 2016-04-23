package hk.gavin.navik.core;

import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import hk.gavin.navik.R;
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

    private NavigationStateDecorator mNavigationState;

    public void setNavigationState(NavigationStateDecorator navigationState) {
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
        mVisualAdvice.setImageBitmap(mNavigationState.visualAdviceImage());
        mDistanceToNextAdvice.setText(
                FormattingUtility.formatDistanceReadableRounded(mNavigationState.object.distanceToNextAdvice)
        );
        mNextStreetName.setText(mNavigationState.object.nextStreetName);

        mDistanceToDestination.setText(
                FormattingUtility.formatDistanceReadableRounded(mNavigationState.object.distanceToDestination)
        );
        mCurrentSpeed.setText(
                FormattingUtility.formatSpeedReadable(mNavigationState.object.currentSpeed)
        );

        // Determine background color
        switch (mNavigationState.turnLevel()) {
            case Safe: {
                mContainer.setBackgroundColor(mColorSafe);
                break;
            }
            case Soon: {
                mContainer.setBackgroundColor(mColorSoon);
                break;
            }
            case Immediate: {
                mContainer.setBackgroundColor(mColorImmediate);
                break;
            }
        }
    }

}

package hk.gavin.navik.ui.presenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import hk.gavin.navik.R;
import hk.gavin.navik.ui.widget.NKElevationChart;

public class RouteAnalysisPresenter extends AbstractPresenter {

    @Bind(R.id.distance) TextView mDistance;
    @Bind(R.id.estimatedTime) TextView mEstimatedTime;
    @Bind(R.id.elevationChart) NKElevationChart mNKElevationChart;

    @BindColor(R.color.colorPrimary) int mColorPrimary;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNKElevationChart.setColor(mColorPrimary);
        mNKElevationChart.setData(SAMPLE_ELEVATION_DATA);
    }

    private static float[] SAMPLE_ELEVATION_DATA = {
            7.3f, 8, 8, 8, 8, 8, 10.6f, 6.6f, 3.8f, 6.2f, 13, 14.2f, 8.6f, 5.75f, 5.75f, 12.4f, 12.4f, 12.4f, 20.2f,
            8.8f, 4.25f, 4.25f, 3.75f, 3.4f, 6.2f, 12.4f, 17.4f, 16.2f, 7.4f, 8.4f, 5.2f, 3, 0, 7, 0.8f, 0.8f, 0.8f,
            0.25f, 0.8f, 0.8f, 0.8f, 1.2f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5.25f, 5.25f, 5.25f, 5.25f, 5.25f, 5.25f, 5.8f,
            5.8f, 5.8f, 11.4f, 11.4f, 11.4f, 21.2f, 5.6f, 9.8f, 10.6f, 11.8f, 11.6f, 6.6f, 6.6f, 4, 4, 4, 0.25f, 1.33f,
            1.33f, 1.33f, 0, 3, 3, 2.75f};
}

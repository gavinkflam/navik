package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "m")
public class RouteAnalysisFragment extends AbstractHomeUiFragment {

    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.distance) TextView mDistance;
    @Bind(R.id.estimatedTime) TextView mEstimatedTime;
    @Bind(R.id.elevationChart) LineChart mElevationChart;

    @BindColor(R.color.colorPrimary) int mColorPrimary;

    private Optional<NKDirections> mDirections = Optional.absent();
    @Getter private final int mLayoutResId = R.layout.fragment_route_analysis;

    public RouteAnalysisFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Update title and back button display
        getController().setActionBarTitle(R.string.route_analysis);
        getController().setDisplayHomeAsUp(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup chart
        List<Entry> entryList = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < SAMPLE_ELEVATION_DATA.length; i++) {
            entryList.add(new Entry(SAMPLE_ELEVATION_DATA[i], i));
            labels.add("" + i);
        }

        LineDataSet dataSet = new LineDataSet(entryList, "Elevation in Meters");
        dataSet.setColor(mColorPrimary);
        dataSet.setFillColor(mColorPrimary);
        dataSet.setDrawCubic(true);
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        LineData data = new LineData(labels, dataSet);
        mElevationChart.setData(data);

        // Customize chart
        mElevationChart.setDescription("");
        mElevationChart.setScaleEnabled(false);
        mElevationChart.getXAxis().setDrawLabels(false);
        mElevationChart.getXAxis().setDrawGridLines(false);
        mElevationChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mElevationChart.getAxisLeft().setDrawGridLines(false);
        mElevationChart.getAxisRight().setDrawAxisLine(false);
        mElevationChart.getAxisRight().setDrawGridLines(false);
        mElevationChart.getAxisRight().setDrawLabels(false);
        mElevationChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static float[] SAMPLE_ELEVATION_DATA = {
            7.3f, 8, 8, 8, 8, 8, 10.6f, 6.6f, 3.8f, 6.2f, 13, 14.2f, 8.6f, 5.75f, 5.75f, 12.4f, 12.4f, 12.4f, 20.2f,
            8.8f, 4.25f, 4.25f, 3.75f, 3.4f, 6.2f, 12.4f, 17.4f, 16.2f, 7.4f, 8.4f, 5.2f, 3, 0, 7, 0.8f, 0.8f, 0.8f,
            0.25f, 0.8f, 0.8f, 0.8f, 1.2f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5.25f, 5.25f, 5.25f, 5.25f, 5.25f, 5.25f, 5.8f,
            5.8f, 5.8f, 11.4f, 11.4f, 11.4f, 21.2f, 5.6f, 9.8f, 10.6f, 11.8f, 11.6f, 6.6f, 6.6f, 4, 4, 4, 0.25f, 1.33f,
            1.33f, 1.33f, 0, 3, 3, 2.75f};
}

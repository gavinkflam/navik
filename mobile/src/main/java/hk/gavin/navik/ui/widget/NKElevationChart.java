package hk.gavin.navik.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.base.Optional;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;

import java.util.ArrayList;
import java.util.List;

public class NKElevationChart extends LineChart {

    private Optional<Integer> mColor = Optional.absent();

    public NKElevationChart(Context context) {
        super(context);
        configureChartStyle();
    }

    public NKElevationChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureChartStyle();
    }

    public NKElevationChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        configureChartStyle();
    }

    private void configureChartStyle() {
        this.setDescription("");
        this.setScaleEnabled(false);
        this.getXAxis().setDrawLabels(false);
        this.getXAxis().setDrawGridLines(false);
        this.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        this.getAxisLeft().setDrawGridLines(false);
        this.getAxisRight().setDrawAxisLine(false);
        this.getAxisRight().setDrawGridLines(false);
        this.getAxisRight().setDrawLabels(false);
    }

    public void setColor(int color) {
        mColor = Optional.of(color);
    }

    public void setData(float[] data) {
        List<Entry> entryList = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        // Construct entry list and labels
        for (int i = 0; i < data.length; i++) {
            entryList.add(new Entry(data[i], i));
            labels.add(Float.toString(i));
        }

        // Construct data set
        LineDataSet dataSet = new LineDataSet(entryList, "Elevation in Meters");
        dataSet.setDrawCubic(true);
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        if (mColor.isPresent()) {
            dataSet.setColor(mColor.get());
            dataSet.setFillColor(mColor.get());
        }

        // Set chart data
        this.setData(new LineData(labels, dataSet));
    }

    public void setData(double[] data) {
        setData(Floats.toArray(Doubles.asList(data)));
    }
}

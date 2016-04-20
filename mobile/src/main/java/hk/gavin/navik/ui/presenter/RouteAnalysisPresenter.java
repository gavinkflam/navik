package hk.gavin.navik.ui.presenter;

import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.elevation.NKElevationProvider;
import hk.gavin.navik.core.location.NKLocation;
import hk.gavin.navik.core.location.NKLocationUtil;
import hk.gavin.navik.ui.decorator.NKDirectionsDecorator;
import hk.gavin.navik.ui.widget.NKElevationChart;
import org.jdeferred.DoneCallback;

import javax.inject.Inject;

public class RouteAnalysisPresenter extends AbstractPresenter implements DoneCallback<NKLocation[]> {

    private Optional<NKDirectionsDecorator> mDirections = Optional.absent();

    @Inject NKElevationProvider mElevationProvider;

    @Bind(R.id.distance) TextView mDistance;
    @Bind(R.id.estimatedTime) TextView mEstimatedTime;
    @Bind(R.id.elevationChart) NKElevationChart mNKElevationChart;

    @BindColor(R.color.colorPrimary) int mColorPrimary;

    public void setDirections(NKDirections directions) {
        mDirections = Optional.of(new NKDirectionsDecorator(directions));
        invalidate();
    }

    @Override
    public void invalidate() {
        if (!mDirections.isPresent() || mElevationProvider == null || mDistance == null) {
            return;
        }

        mDistance.setText(mDirections.get().distance());
        mEstimatedTime.setText(mDirections.get().estimatedTime());
        mElevationProvider
                .requestElevation(mDirections.get().object.locations)
                .done(this);
    }

    @Override
    public void onDone(NKLocation[] result) {
        mNKElevationChart.setColor(mColorPrimary);
        mNKElevationChart.setData(NKLocationUtil.extractElevations(result));
    }
}

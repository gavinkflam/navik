package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class RouteAnalysisFragment extends AbstractHomeUiFragment {

    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    @Bind(R.id.distance) TextView mDistance;
    @Bind(R.id.estimatedTime) TextView mEstimatedTime;

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
    public void onResume() {
        super.onResume();
    }
}

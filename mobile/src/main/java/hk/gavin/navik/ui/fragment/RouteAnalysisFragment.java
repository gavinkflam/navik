package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import android.view.View;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.ui.presenter.RouteAnalysisPresenter;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public class RouteAnalysisFragment extends AbstractHomeUiFragment {

    @Inject NKReverseGeocoder mReverseGeocoder;
    @Inject NKInteractiveDirectionsProvider mDirectionsProvider;

    private Optional<NKDirections> mDirections = Optional.absent();
    RouteAnalysisPresenter mRouteAnalysisPresenter = new RouteAnalysisPresenter();

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

        // Presenters
        mRouteAnalysisPresenter.setContext(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRouteAnalysisPresenter.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

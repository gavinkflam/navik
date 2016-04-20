package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import android.view.View;
import hk.gavin.navik.R;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.ui.presenter.RouteAnalysisPresenter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class RouteAnalysisFragment extends AbstractHomeUiFragment {

    RouteAnalysisPresenter mRouteAnalysisPresenter = new RouteAnalysisPresenter();

    @Getter private final int mLayoutResId = R.layout.fragment_route_analysis;

    public RouteAnalysisFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inject dependencies for presenters
        component().inject(mRouteAnalysisPresenter);
        mRouteAnalysisPresenter.invalidate();

        // Update title and back button display
        getController().setActionBarTitle(R.string.route_analysis);
        getController().setDisplayHomeAsUp(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRouteAnalysisPresenter.onViewCreated(view, savedInstanceState);

        // Display directions data
        NKDirections directions =
                (NKDirections) getRequestData().get().getSerializableExtra(UiContract.DataKey.DIRECTIONS);
        mRouteAnalysisPresenter.setDirections(directions);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

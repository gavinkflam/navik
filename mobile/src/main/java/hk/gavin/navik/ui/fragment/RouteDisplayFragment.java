package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import hk.gavin.navik.R;
import hk.gavin.navik.ui.presenter.RouteDisplayPresenter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class RouteDisplayFragment extends AbstractHomeUiFragment {

    RouteDisplayPresenter mRouteDisplayPresenter = new RouteDisplayPresenter();

    @Getter private final int mLayoutResId = R.layout.fragment_route_display;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        component().inject(mRouteDisplayPresenter);
        mRouteDisplayPresenter.setMap(getController().getMap());
    }

    @Override
    public void onResume() {
        super.onResume();
        mRouteDisplayPresenter.onResume();
    }

    @Override
    public void onPause() {
        mRouteDisplayPresenter.onPause();
        super.onPause();
    }
}

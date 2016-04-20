package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import hk.gavin.navik.injection.HomeComponent;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.fragmentcontroller.HomeFragmentController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public abstract class AbstractHomeUiFragment extends AbstractUiFragment {

    @Getter(AccessLevel.PROTECTED) @Inject HomeFragmentController mController;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (this instanceof LocationSelectionFragment) {
            component().inject((LocationSelectionFragment) this);
        }
        else if (this instanceof NavigationFragment) {
            component().inject((NavigationFragment) this);
        }
        else if (this instanceof RouteDisplayFragment) {
            component().inject((RouteDisplayFragment) this);
        }
        else if (this instanceof RoutePlannerFragment) {
            component().inject((RoutePlannerFragment) this);
        }
        else if (this instanceof RouteAnalysisFragment) {
            component().inject((RouteAnalysisFragment) this);
        }
    }

    public HomeComponent component() {
        return ((HomeActivity) getActivity()).component();
    }
}

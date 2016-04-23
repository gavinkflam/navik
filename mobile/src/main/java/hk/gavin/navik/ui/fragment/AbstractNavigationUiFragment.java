package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import hk.gavin.navik.injection.NavigationComponent;
import hk.gavin.navik.ui.activity.NavigationActivity;
import hk.gavin.navik.ui.fragmentcontroller.NavigationFragmentController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Accessors(prefix = "m")
public abstract class AbstractNavigationUiFragment extends AbstractUiFragment {

    @Getter(AccessLevel.PROTECTED) @Inject NavigationFragmentController mController;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (this instanceof NavigationFragment) {
            component().inject((NavigationFragment) this);
        }
    }

    public NavigationComponent component() {
        return ((NavigationActivity) getActivity()).component();
    }
}

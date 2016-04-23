package hk.gavin.navik.ui.fragmentcontroller;

import hk.gavin.navik.R;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.core.map.NKMapFragment;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;
import hk.gavin.navik.ui.activity.NavigationActivity;
import hk.gavin.navik.ui.fragment.NavigationFragment;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class NavigationFragmentController extends FragmentController<NavigationActivity> {

    @Getter private NKMapFragment mMap;

    public NavigationFragmentController(NavigationActivity activity) {
        super(activity);
    }

    public void initialize() {
        super.initialize();

        setEmptyRequest();
        mMap = replaceFragment(
                R.id.navigationMap, NKSkobblerMapFragment.class, UiContract.FragmentTag.NAVIGATION_MAP, false, false
        );
        getActivity().component().inject((NKSkobblerMapFragment) mMap);
        replaceFragment(R.id.contentFrame, NavigationFragment.class, UiContract.FragmentTag.NAVIGATION, false, true);
    }
}

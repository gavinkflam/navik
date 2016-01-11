package hk.gavin.navik.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.location.NavikLocation;
import hk.gavin.navik.widget.LocationSelector;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mStartingPoint.setLocation(
                new NavikLocation(getString(R.string.current_location), 0, 0)
        );
    }
}

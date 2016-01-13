package hk.gavin.navik.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import hk.gavin.navik.R;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.activity.SelectLocationOnMapActivity;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.location.NavikLocation;
import hk.gavin.navik.widget.LocationSelector;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.startingPoint) LocationSelector mStartingPoint;
    @Bind(R.id.destination) LocationSelector mDestination;

    StartingPointHandler mStartingPointHandler = new StartingPointHandler();
    DestinationPointHandler mDestinationPointHandler = new DestinationPointHandler();

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
        mDestination.setLocation(
                new NavikLocation("Sam Mun Tsai", 0, 0)
        );

        mStartingPoint.setOnLocationUpdatedListener(mStartingPointHandler);
        mStartingPoint.setOnMenuItemClickListener(mStartingPointHandler);

        mDestination.setOnLocationUpdatedListener(mDestinationPointHandler);
        mDestination.setOnMenuItemClickListener(mDestinationPointHandler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public static class RequestCode {
        public static int STARTING_POINT_LOCATION = 1;
        public static int STARTING_POINT_HISTORY = 1;
        public static int DESTINATION_LOCATION = 3;
        public static int DESTINATION_HISTORY = 3;
    }

    private class StartingPointHandler implements LocationSelector.OnLocationUpdatedListener,
            LocationSelector.OnMenuItemClickListener {

        @Override
        public void onLocationUpdated(NavikLocation location) {

        }

        @Override
        public void onCurrentLocationClicked() {

        }

        @Override
        public void onHistoryClicked() {

        }

        @Override
        public void onSelectLocationOnMapClicked() {
            Intent startActivity = new Intent(HomeFragment.this.getContext(), SelectLocationOnMapActivity.class);
            startActivity.putExtra(UiContract.DATA_TITLE, getString(R.string.select_starting_point));
            startActivityForResult(startActivity, RequestCode.STARTING_POINT_LOCATION);
        }
    }

    private class DestinationPointHandler implements LocationSelector.OnLocationUpdatedListener,
            LocationSelector.OnMenuItemClickListener {

        @Override
        public void onLocationUpdated(NavikLocation location) {

        }

        @Override
        public void onCurrentLocationClicked() {

        }

        @Override
        public void onHistoryClicked() {

        }

        @Override
        public void onSelectLocationOnMapClicked() {
            Intent startActivity = new Intent(HomeFragment.this.getContext(), SelectLocationOnMapActivity.class);
            startActivity.putExtra(UiContract.DATA_TITLE, getString(R.string.select_destination));
            startActivityForResult(startActivity, RequestCode.DESTINATION_LOCATION);
        }
    }

}

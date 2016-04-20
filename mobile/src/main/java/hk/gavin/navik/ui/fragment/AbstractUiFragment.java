package hk.gavin.navik.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.google.common.base.Optional;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class AbstractUiFragment extends Fragment {

    @Getter private Optional<Integer> mRequestCode = Optional.absent();
    @Getter private Optional<Intent> mRequestData = Optional.absent();

    abstract protected int getLayoutResId();

    public void onRequestAvailable(int requestCode, Optional<Intent> requestData) {
        mRequestCode = Optional.of(requestCode);
        mRequestData = requestData;
    }

    public void onResultAvailable(Optional<Integer> requestCode, int resultCode, Optional<Intent> resultData) {
        // Do nothing
    }

    public void onBackPressed() {
        // Do nothing
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

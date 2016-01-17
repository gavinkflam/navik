package hk.gavin.navik.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import butterknife.ButterKnife;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class AbstractUiFragment extends Fragment {

    @Getter(AccessLevel.PROTECTED) private boolean mActivityCreated = false;
    @Getter(AccessLevel.PROTECTED) private boolean mViewsInjected = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivityCreated = true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mViewsInjected = true;
    }

    public void onViewVisible() {

    }

    public void onResultAvailable(int requestCode, int resultCode, Intent data) {

    }

    public void onBackPressed() {

    }
}

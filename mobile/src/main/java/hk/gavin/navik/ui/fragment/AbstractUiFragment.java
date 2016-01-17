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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class AbstractUiFragment extends Fragment {

    @Getter(AccessLevel.PROTECTED) private boolean mActivityCreated = false;
    @Getter(AccessLevel.PROTECTED) private boolean mViewsInjected = false;
    @Getter(AccessLevel.PROTECTED) private boolean mInitialized = false;
    @Getter(AccessLevel.PROTECTED) private boolean mViewsInitialized = false;

    abstract int getLayoutResId();
    abstract void onInjectComponent();

    public void onInitialize() {
        mInitialized = true;
    }

    public void onInitializeViews() {
        mViewsInitialized = true;
    }

    public void onViewVisible() {
        // Do nothing
    }

    public void onResultAvailable(int requestCode, int resultCode, Intent data) {
        // Do nothing
    }

    public void onBackPressed() {
        // Do nothing
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInjectComponent();
        mActivityCreated = true;

        onInitialize();
        onInitializeViews();
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
        mViewsInjected = true;

        onInitialize();
        onInitializeViews();
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

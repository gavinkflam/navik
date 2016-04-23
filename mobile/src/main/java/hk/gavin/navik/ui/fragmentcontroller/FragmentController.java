package hk.gavin.navik.ui.fragmentcontroller;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.ui.fragment.AbstractUiFragment;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class FragmentController<T extends AppCompatActivity> implements FragmentManager.OnBackStackChangedListener {

    @Getter private T mActivity;
    @Getter private FragmentManager mManager;

    @Getter private AbstractUiFragment mCurrentFragment;
    @Getter private Optional<Integer> mRequestCode = Optional.absent();
    @Getter private Optional<Intent> mRequestData = Optional.absent();
    @Getter private Optional<Integer> mResultCode = Optional.absent();
    @Getter private Optional<Intent> mResultData = Optional.absent();

    public FragmentController(T activity) {
        mActivity = activity;
        mManager = mActivity.getSupportFragmentManager();
    }

    public void initialize() {
        mManager.addOnBackStackChangedListener(this);
    }

    public void showMessage(String notification) {
        Toast.makeText(getActivity(), notification, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(int notificationRes) {
        showMessage(getActivity().getString(notificationRes));
    }

    public void goBack() {
        mActivity.onBackPressed();
    }

    public void setRequest(int requestCode) {
        mRequestCode = Optional.of(requestCode);
        mRequestData = Optional.absent();
    }

    public void setRequest(int requestCode, Intent requestData) {
        mRequestCode = Optional.of(requestCode);
        mRequestData = Optional.of(requestData);
    }

    public void setEmptyRequest() {
        mRequestCode = Optional.absent();
        mRequestData = Optional.absent();
    }

    public void setResult(int resultCode) {
        mResultCode = Optional.of(resultCode);
        mResultData = Optional.absent();
    }

    public void setResult(int resultCode, Intent result) {
        mResultCode = Optional.of(resultCode);
        mResultData = Optional.of(result);
    }

    public void setEmptyResult() {
        mResultCode = Optional.absent();
        mResultData = Optional.absent();
    }

    public void setActionBarTitle(@StringRes int stringRes) {
        getActionBar().setTitle(stringRes);
    }

    public void setDisplayHomeAsUp(boolean displayHomeAsUp) {
        getActionBar().setDisplayShowHomeEnabled(displayHomeAsUp);
        getActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUp);
    }

    protected  <T extends AbstractUiFragment> T replaceFragment(
            int id, Class<T> fClass, String tag, boolean transition, boolean addToBackStack) {
        FragmentTransaction transaction = mManager.beginTransaction();
        T fragment = getFragment(fClass, tag);

        transaction.replace(id, fragment, tag);
        if (transition) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        if (addToBackStack) {
            transaction.addToBackStack(UiContract.FragmentTag.SELECT_STARTING_POINT);
        }
        transaction.commit();

        return fragment;
    }

    protected <T extends AbstractUiFragment> T getFragment(Class<T> fClass, String tag) {
        Optional<T> fragment = Optional.fromNullable(
                (T) mManager.findFragmentByTag(tag)
        );

        if (!fragment.isPresent()) {
            try {
                fragment = Optional.of(fClass.newInstance());
            }
            catch (Exception e) {
                // Do nothing
            }
        }
        if (mRequestCode.isPresent()) {
            fragment.get().onRequestAvailable(mRequestCode.get(), mRequestData);
        }

        return fragment.get();
    }

    @Override
    public void onBackStackChanged() {
        Optional<AbstractUiFragment> fragment = Optional.fromNullable(
                (AbstractUiFragment) mManager.findFragmentById(R.id.contentFrame)
        );
        if (fragment.isPresent()) {
            Optional<Integer> previousRequestCode = Optional.absent();
            if (mCurrentFragment != null) {
                previousRequestCode = mCurrentFragment.getRequestCode();
            }

            mCurrentFragment = fragment.get();
            if (mResultCode.isPresent()) {
                mCurrentFragment.onResultAvailable(previousRequestCode, mResultCode.get(), mResultData);

                this.setEmptyRequest();
                this.setEmptyResult();
            }
        }
    }

    protected ActionBar getActionBar() {
        return mActivity.getSupportActionBar();
    }
}

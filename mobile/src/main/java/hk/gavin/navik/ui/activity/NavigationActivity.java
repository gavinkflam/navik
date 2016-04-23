package hk.gavin.navik.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.common.base.Optional;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.injection.DaggerNavigationComponent;
import hk.gavin.navik.injection.NavigationComponent;
import hk.gavin.navik.injection.NavigationModule;
import hk.gavin.navik.ui.fragmentcontroller.NavigationFragmentController;

import javax.inject.Inject;

public class NavigationActivity extends AppCompatActivity
        implements AbstractNavikActivity<NavigationComponent> {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Inject NavigationFragmentController mController;
    private Optional<NavigationComponent> mComponent = Optional.absent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);

        setContentView(R.layout.activity_navigation);
        onViewCreated();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onViewCreated() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mController.initialize();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }
        else {
            mController.getCurrentFragment().onBackPressed();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                return mController.getCurrentFragment().onOptionsItemSelected(item);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public NavigationComponent component() {
        if (!mComponent.isPresent()) {
            mComponent = Optional.of(
                    DaggerNavigationComponent.builder()
                            .applicationComponent(NKApplication.getInstance().component())
                            .navigationModule(new NavigationModule(this))
                            .build()
            );
            mComponent.get().inject(this);
        }

        return mComponent.get();
    }
}

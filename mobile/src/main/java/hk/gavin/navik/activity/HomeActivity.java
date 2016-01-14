package hk.gavin.navik.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.injection.DaggerHomeComponent;
import hk.gavin.navik.injection.HomeComponent;
import hk.gavin.navik.injection.HomeModule;
import hk.gavin.navik.ui.HomeController;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity
        implements AbstractNavikActivity<HomeComponent> {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Inject HomeController mController;
    private HomeComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);

        setContentView(R.layout.activity_home);
        onViewCreated();
    }

    protected void onViewCreated() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mController.initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_offline_data: {
                return true;
            }
            case R.id.action_settings: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public HomeComponent component() {
        if (mComponent == null) {
            mComponent = DaggerHomeComponent.builder()
                    .applicationComponent(NKApplication.getInstance().component())
                    .homeModule(new HomeModule(this))
                    .build();
            mComponent.inject(this);
        }
        return mComponent;
    }
}

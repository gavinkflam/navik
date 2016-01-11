package hk.gavin.navik.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NavikApplication;
import hk.gavin.navik.injection.ActivityModule;
import hk.gavin.navik.injection.DaggerHomeComponent;
import hk.gavin.navik.injection.HomeComponent;
import hk.gavin.navik.location.NavikLocationProvider;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity
        implements AbstractNavikActivity<HomeComponent> {

    private HomeComponent mComponent;

    @Bind(R.id.startBikeNavigation) FloatingActionButton mStartBikeNavigation;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Inject NavikLocationProvider mLocationProvider;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @OnClick(R.id.startBikeNavigation)
    void startBikeNavigation() {
        Intent startNavigation = new Intent(this, NavigationActivity.class);
        startActivity(startNavigation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public HomeComponent component() {
        if (mComponent == null) {
            mComponent = DaggerHomeComponent.builder()
                    .applicationComponent(NavikApplication.getInstance().component())
                    .activityModule(new ActivityModule(this))
                    .build();
            mComponent.inject(this);
        }
        return mComponent;
    }
}

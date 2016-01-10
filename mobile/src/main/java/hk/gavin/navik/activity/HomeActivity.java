package hk.gavin.navik.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class HomeActivity extends AppCompatActivity {

    private HomeComponent mComponent;

    @Bind(R.id.startBikeNavigation)
    FloatingActionButton mStartBikeNavigation;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);

        setContentView(R.layout.activity_main);
        onViewCreated();
    }

    protected void onViewCreated() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @OnClick(R.id.startBikeNavigation)
    void startBikeNavigation() {
        Snackbar
                .make(mStartBikeNavigation, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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

package hk.gavin.navik.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NavikApplication;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.fragment.NavigationFragment;
import hk.gavin.navik.injection.ActivityModule;
import hk.gavin.navik.injection.DaggerNavigationComponent;
import hk.gavin.navik.injection.NavigationComponent;

public class NavigationActivity extends AppCompatActivity {

    private NavigationComponent mComponent;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    NavigationFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);

        setContentView(R.layout.activity_navigation);
        onViewCreated();
    }

    protected void onViewCreated() {
        ButterKnife.bind(this);
        mFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelNavigation();
            }

        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.navigation);
    }

    public void cancelNavigation() {
        setResult(UiContract.RESULT_CANCEL);
        finish();
    }

    public NavigationComponent component() {
        if (mComponent == null) {
            mComponent = DaggerNavigationComponent.builder()
                    .applicationComponent(NavikApplication.getInstance().component())
                    .activityModule(new ActivityModule(this))
                    .build();
            mComponent.inject(this);
        }
        return mComponent;
    }
}

package hk.gavin.navik.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NavikApplication;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.injection.ActivityModule;
import hk.gavin.navik.injection.DaggerSelectLocationOnMapComponent;
import hk.gavin.navik.injection.SelectLocationOnMapComponent;

public class SelectLocationOnMapActivity extends AppCompatActivity {

    private SelectLocationOnMapComponent mComponent;

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);

        setContentView(R.layout.activity_select_location_on_map);
        onViewCreated();
    }

    protected void onViewCreated() {
        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissLocationSelection();
            }

        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(UiContract.DATA_TITLE));
    }

    public void dismissLocationSelection() {
        setResult(UiContract.RESULT_CANCEL);
        finish();
    }

    @OnClick(R.id.confirm)
    void confirmLocationSelection() {
        setResult(UiContract.RESULT_OK);
        finish();
    }

    public SelectLocationOnMapComponent component() {
        if (mComponent == null) {
            mComponent = DaggerSelectLocationOnMapComponent.builder()
                    .applicationComponent(NavikApplication.getInstance().component())
                    .activityModule(new ActivityModule(this))
                    .build();
            mComponent.inject(this);
        }
        return mComponent;
    }
}

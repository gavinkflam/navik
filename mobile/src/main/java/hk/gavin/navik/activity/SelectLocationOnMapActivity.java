package hk.gavin.navik.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.contract.UiContract;
import hk.gavin.navik.fragment.SelectLocationOnMapFragment;
import hk.gavin.navik.injection.ActivityModule;
import hk.gavin.navik.injection.DaggerSelectLocationOnMapComponent;
import hk.gavin.navik.injection.SelectLocationOnMapComponent;

public class SelectLocationOnMapActivity extends AppCompatActivity
        implements AbstractNavikActivity<SelectLocationOnMapComponent> {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    private SelectLocationOnMapFragment mSelectLocationOnMapFragment;

    private SelectLocationOnMapComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);

        setContentView(R.layout.activity_select_location_on_map);
        onViewCreated();
    }

    protected void onViewCreated() {
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(UiContract.DataKey.TITLE));

        mSelectLocationOnMapFragment = (SelectLocationOnMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.select_location_on_map_fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                dismissLocationSelection();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void dismissLocationSelection() {
        setResult(UiContract.ResultCode.CANCEL);
        finish();
    }

    @OnClick(R.id.confirm)
    void confirmLocationSelection() {
        Intent result = getIntent();
        result.putExtra(UiContract.DataKey.LOCATION, mSelectLocationOnMapFragment.getLocation());
        setResult(UiContract.ResultCode.OK, result);

        finish();
    }

    public SelectLocationOnMapComponent component() {
        if (mComponent == null) {
            mComponent = DaggerSelectLocationOnMapComponent.builder()
                    .applicationComponent(NKApplication.getInstance().component())
                    .activityModule(new ActivityModule(this))
                    .build();
            mComponent.inject(this);
        }
        return mComponent;
    }
}

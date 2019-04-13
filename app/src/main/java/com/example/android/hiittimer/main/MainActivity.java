package com.example.android.hiittimer.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.hiittimer.BuildConfig;
import com.example.android.hiittimer.detail.DetailActivity;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.timer.TimerActivity;
import com.example.android.hiittimer.ui.edit.EditActivity;
import com.example.android.hiittimer.ui.edit.EditFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String ASSET_KEY = "asset";

    private AdView mAdView;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, BuildConfig.adMobKey);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);

        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.getOpenDetailEvent().observed(this, this::startDetailActivity);
        mViewModel.getNewAssetEvent().observed(this, aVoid -> addNewAsset());
        mViewModel.getOpenTimerActivity().observed(this, this::startTimerActivity);

        setUpSharedPreferences();
    }

    private void setUpSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        int id = sharedPreferences.getInt("id", 1);
        mViewModel.asset(id).observe(this,
                asset ->
                {
                    mViewModel.setMutableAsset(asset);
                    mViewModel.setAsset(asset);
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void startDetailActivity(Asset asset) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(ASSET_KEY, asset.getId());
        startActivity(intent);
    }

    public void startTimerActivity(View view) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra(ASSET_KEY, mViewModel.getAsset());
        startActivity(intent);
    }

    private void addNewAsset() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditFragment.ARG_EDIT_KEY, true);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("default_asset")) {
        }
        sharedPreferences.getInt(key, 1);
    }
}

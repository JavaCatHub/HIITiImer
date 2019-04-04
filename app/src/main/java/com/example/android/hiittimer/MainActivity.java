package com.example.android.hiittimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.hiittimer.model.Asset;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

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
        mViewModel.getOpenDetailEditEvent().observed(this, this::startEditActivity);
        mViewModel.getOpenTimerActivity().observed(this,this::startTimerActivity);
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

    public void startEditActivity(Object obj) {
        Intent intent = new Intent(this,DetailActivity.class);
        if (obj instanceof Asset) {
            Asset asset = (Asset) obj;
            intent.putExtra(ASSET_KEY, asset);
        }else if(obj instanceof View){
            Asset asset = new Asset();
            intent.putExtra(ASSET_KEY,asset);
        }else {
            Timber.e("There happened to a problem");
            return;
        }
        startActivity(intent);
    }

    public void startTimerActivity(View view){
        Intent intent = new Intent(this,TimerActivity.class);
        Asset asset = new Asset();
        asset.setDefaultMyself();
        intent.putExtra(ASSET_KEY,asset);
        startActivity(intent);
    }

}

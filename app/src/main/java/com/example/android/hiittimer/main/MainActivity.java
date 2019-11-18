package com.example.android.hiittimer.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.hiittimer.BuildConfig;
import com.example.android.hiittimer.CreateMenuAsyncTask;
import com.example.android.hiittimer.databinding.ActivityMainBinding;
import com.example.android.hiittimer.detail.DetailActivity;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.timer.TimerActivity;
import com.example.android.hiittimer.ui.edit.EditActivity;
import com.example.android.hiittimer.ui.edit.EditFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import timber.log.Timber;

// TODO 2 widgetのプラスを押したら新しいアセットを作成editする画面に飛ぶ
// TODO 3 詳細と編集画面にMainActivityに戻れるボタンを作成する


public class MainActivity extends AppCompatActivity {
    public static final String ASSET_KEY = "getAssetById";
    private static final int RC_SIGN_IN = 1;

    private ActivityMainBinding binding;
    private AdView mAdView;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainActivityViewModel mViewModel;
    private GoogleSignInAccount account;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        initializeAdView();
        initializeToolbar();
        initializeViewPager();
        initializeEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("onResume");
    }

    private void initializeEvent() {
        mViewModel.getOpenDetailEvent().observed(this, this::startDetailActivity);
        mViewModel.getNewAssetEvent().observed(this, aVoid -> addNewAsset());
        mViewModel.getOpenTimerActivity().observed(this, this::startTimerActivity);
    }

    private void initializeViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initializeAdView() {
        MobileAds.initialize(this, BuildConfig.adMobKey);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initializeToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.i("onStart");
        account = GoogleSignIn.getLastSignedInAccount(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Timber.i("On prepare Options menu");
        if (account != null) {
            MenuItem settingsItem = menu.findItem(R.id.login);
            CreateMenuAsyncTask createMenuAsyncTask = new CreateMenuAsyncTask(this, settingsItem);
            createMenuAsyncTask.execute(account.getPhotoUrl());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Timber.i("onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login: {
                if (GoogleSignIn.getLastSignedInAccount(this) == null) {
                    signIn(getGoogleSignInClient());
                } else {
                    signOut(getGoogleSignInClient());
                }
            }
        }
        return false;
    }

    private GoogleSignInClient getGoogleSignInClient() {
        if (gso == null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                    .build();
        }
        return GoogleSignIn.getClient(this, gso);
    }

    private void signIn(GoogleSignInClient client) {
        Intent intent = client.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void signOut(GoogleSignInClient client) {
        client.revokeAccess().addOnCompleteListener(this, task -> {
            account = null;
            invalidateOptionsMenu();
            Snackbar.make(binding.mainActivityView, "Revoked", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            invalidateOptionsMenu();
        } catch (ApiException e) {
            Timber.w("signInResult: failed code=%s", e.getStatusCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
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
}

package com.example.android.hiittimer.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.android.hiittimer.ActivityUtils;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.ActivityDetailBinding;
import com.example.android.hiittimer.main.MainActivity;
import com.google.android.gms.ads.AdRequest;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    private DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setSupportActionBar(binding.detailToolbar);
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        DetailFragment fragment = findOrCreateFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment,R.id.contentFrame);

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
    }

    private DetailFragment findOrCreateFragment() {

        int assetId = getIntent().getIntExtra(MainActivity.ASSET_KEY, 0);

        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = DetailFragment.newInstance(assetId);
        }
        return fragment;
    }
}

package com.example.android.hiittimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.android.hiittimer.databinding.ActivityDetailBinding;
import com.example.android.hiittimer.model.Asset;
import com.google.android.gms.ads.AdRequest;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        setSupportActionBar(binding.detailToolbar);
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        Asset asset = getIntent().getParcelableExtra(MainActivity.ASSET_KEY);
        binding.setAsset(asset);
    }
}

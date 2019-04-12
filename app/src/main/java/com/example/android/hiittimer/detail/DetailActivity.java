package com.example.android.hiittimer.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.hiittimer.ActivityUtils;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.ActivityDetailBinding;
import com.example.android.hiittimer.main.MainActivity;
import com.example.android.hiittimer.ui.edit.EditFragment;
import com.google.android.gms.ads.AdRequest;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    private DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        AdRequest adRequest = new AdRequest.Builder().build();
        setSupportActionBar(binding.detailToolbar);
        binding.adView.loadAd(adRequest);

        DetailFragment fragment = findOrCreateFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment,R.id.contentFrame);

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        setOnClickListener();
        initEvent();

    }

    private void initEvent() {
        viewModel.getEditAssetEvent().observed(this, aVoid -> {
            Intent intent = new Intent();
            intent.putExtra(EditFragment.ARG_EDIT_KEY,true);
            intent.putExtra(EditFragment.ARG_EDIT_ASSET_ID,viewModel.getAssetId());
            startActivity(intent);
        });
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

    private void setOnClickListener(){
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getEditAssetEvent().call();
            }
        });
    }
}

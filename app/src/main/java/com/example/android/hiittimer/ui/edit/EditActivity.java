package com.example.android.hiittimer.ui.edit;

import android.os.Bundle;
import android.view.View;

import com.example.android.hiittimer.ActivityUtils;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.EditActivityBinding;
import com.example.android.hiittimer.main.MainActivity;
import com.google.android.gms.ads.AdRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class EditActivity extends AppCompatActivity {

    private EditActivityBinding binding;

    private EditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.edit_activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        setSupportActionBar(binding.editToolbar);
        binding.adView.loadAd(adRequest);
        viewModel = ViewModelProviders.of(this).get(EditViewModel.class);

        EditFragment fragment = findOrCreateFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment,R.id.contentFrame);

        binding.fabSave.setOnClickListener(v -> viewModel.getInsertLiveData().setValue(v));
    }

    private EditFragment findOrCreateFragment() {

        int assetId = getIntent().getIntExtra(MainActivity.ASSET_KEY,0);

        EditFragment fragment = (EditFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = EditFragment.newInstance(
                    assetId
            );
        }
        return fragment;
    }
}

package com.example.android.hiittimer.ui.edit;

import android.os.Bundle;

import com.example.android.hiittimer.ActivityUtils;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.EditActivityBinding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.edit_activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        setSupportActionBar(binding.editToolbar);
        binding.adView.loadAd(adRequest);
        viewModel = ViewModelProviders.of(this).get(EditViewModel.class);

        EditFragment fragment = findOrCreateFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment, R.id.contentFrame);

        binding.fabSave.setOnClickListener(v ->{
                viewModel.getSaveLiveData().call();
                finish();
        });
    }

    private EditFragment findOrCreateFragment() {
        EditFragment fragment = (EditFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = EditFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putInt(EditFragment.ARG_EDIT_ASSET_ID,
                    getIntent().getIntExtra(EditFragment.ARG_EDIT_ASSET_ID, 0));
            bundle.putBoolean(EditFragment.ARG_EDIT_KEY,
                    getIntent().getBooleanExtra(EditFragment.ARG_EDIT_KEY, false));
            fragment.setArguments(bundle);
        }

        return fragment;
    }


}

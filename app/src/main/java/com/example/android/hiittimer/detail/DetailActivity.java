package com.example.android.hiittimer.detail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.hiittimer.ActivityUtils;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.ActivityDetailBinding;
import com.example.android.hiittimer.main.MainActivity;
import com.example.android.hiittimer.ui.edit.EditActivity;
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
                fragment, R.id.contentFrame);

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.getEditAssetEvent().observed(this, aVoid -> initEvent());
        setOnClickListener();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (viewModel.getAsset() != null){
            MenuItem item = menu.findItem(R.id.select);
            if (viewModel.getAsset().isDefault()) {
                item.setIcon(R.drawable.ic_bookmark_white_24dp);
            } else {
                item.setIcon(R.drawable.ic_bookmark_border_white_24dp);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete: {
                new AlertDialog.Builder(this)
                        .setTitle("Are you sure you want to delete this asset?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();
                            viewModel.delete();
                            finish();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        }).show();
                break;
            }
            case R.id.select: {
                if (viewModel.getAsset().isDefault()) {
                    Timber.i("On update false");
                    viewModel.setTrueToFalse();
                } else {
                    Timber.i("On update true");
                    viewModel.updateDefaultAsset(true, viewModel.getAsset().getId());
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void initEvent() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditFragment.ARG_EDIT_KEY, false);
        intent.putExtra(EditFragment.ARG_EDIT_ASSET_ID, viewModel.getAssetId());
        startActivity(intent);
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

    private void setOnClickListener() {
        binding.fab.setOnClickListener(v -> viewModel.getEditAssetEvent().call());
    }
}

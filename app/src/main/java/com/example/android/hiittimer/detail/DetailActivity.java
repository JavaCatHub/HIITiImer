package com.example.android.hiittimer.detail;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
                fragment,R.id.contentFrame);

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.getEditAssetEvent().observed(this, aVoid -> initEvent());
        setOnClickListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete : {
                viewModel.delete();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void initEvent() {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra(EditFragment.ARG_EDIT_KEY,false);
            intent.putExtra(EditFragment.ARG_EDIT_ASSET_ID,viewModel.getAssetId());
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

    private void setOnClickListener(){
        binding.fab.setOnClickListener(v -> viewModel.getEditAssetEvent().call());
    }
}

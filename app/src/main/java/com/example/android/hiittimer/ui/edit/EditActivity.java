package com.example.android.hiittimer.ui.edit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import com.example.android.hiittimer.ActivityUtils;
import com.example.android.hiittimer.wigdet.HIITWidgetProvider;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.EditActivityBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

public class EditActivity extends AppCompatActivity {

    private EditActivityBinding binding;

    private EditViewModel viewModel;

    private static final int OVER_CHARACTER = 111;

    private static final int EMPTY_TITLE = 222;

    private static final int OVER_THOUSAND = 333;

    private static final int OK = 200;

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
    }

    private void onSave() {
        switch (checkAsset()) {
            case OK: {
                viewModel.getSaveLiveData().call();
                HIITWidgetProvider.sendRefreshBroadcast(this);
                finish();
                break;
            }
            case EMPTY_TITLE: {
                showSnackBar("Please enter the title.");
                break;
            }
            case OVER_CHARACTER: {
                showSnackBar("Please make the title 20 characters or less.");
                break;
            }
            case OVER_THOUSAND: {
                showSnackBar("Please make the comment 1000 characters or less");
            }
            default: {
                Timber.i("Something wrong on check title");
            }
        }
    }

    private int checkAsset() {
        if (TextUtils.isEmpty(viewModel.getAsset().getValue().getTitle())) {
            return EMPTY_TITLE;
        } else if (viewModel.getAsset().getValue().getTitle().length() > 20) {
            return OVER_CHARACTER;
        } else if (TextUtils.isEmpty(viewModel.getAsset().getValue().getComment()) && viewModel.getAsset().getValue().getComment().length() > 1000) {
            return OVER_THOUSAND;
        } else {
            return OK;
        }
    }

    private void showSnackBar(String text) {
        Snackbar.make(binding.contentFrame, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to discard this edit?")
                .setPositiveButton("Continue", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Discard", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            onSave();
        }
        return true;
    }
}

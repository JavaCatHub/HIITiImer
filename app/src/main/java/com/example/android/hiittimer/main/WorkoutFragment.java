package com.example.android.hiittimer.main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.FragmentWorkoutBinding;
import com.example.android.hiittimer.model.Asset;

import java.util.ArrayList;
import java.util.List;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;
    private MainActivityViewModel mViewModel;
    private AlertDialog.Builder builder;

    public WorkoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeAlertBuilder(requireActivity());
        mViewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);
        setCurrentAsset();
        reloadAlertBuilder();
        initializeClickListener();
    }

    private void initializeAlertBuilder(FragmentActivity context) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your Asset");
    }

    private void initializeClickListener() {
        binding.currentAsset.setOnClickListener(v -> builder.show());
        binding.selectAssetText.setOnClickListener(v -> builder.show());
        binding.playWorkout.setOnClickListener(v -> mViewModel.getOpenTimerActivity().setValue(v));
    }

    private void reloadAlertBuilder() {
        mViewModel.getAssetList().observe(this, assets ->
                builder.setItems(listToStringArray(assets), (dialog, which) -> {
                    dialog.dismiss();
                    mViewModel.updateDefaultAsset(true, assets.get(which).getId());
                }));
    }

    private void setCurrentAsset() {
        mViewModel.getDefaultAsset().observe(this, asset -> {
            Timber.i("On get Default Asset");
            if (asset != null) {
                showFragment();
                binding.setAsset(asset);
                mViewModel.setAsset(asset);
            } else {
                hideFragment();
            }
        });
    }

    private void showFragment() {
        binding.workOutFragment.setVisibility(View.VISIBLE);
        binding.selectAssetText.setVisibility(View.INVISIBLE);
    }

    private void hideFragment() {
        binding.workOutFragment.setVisibility(View.INVISIBLE);
        binding.selectAssetText.setVisibility(View.VISIBLE);
    }

    private String[] listToStringArray(List<Asset> list) {
        List<String> strings = new ArrayList<>();
        for (Asset asset : list) {
            strings.add(asset.getTitle());
        }
        return strings.toArray(new String[0]);
    }

}

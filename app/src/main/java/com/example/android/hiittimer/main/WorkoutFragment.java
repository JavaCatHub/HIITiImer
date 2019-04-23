package com.example.android.hiittimer.main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.FragmentWorkoutBinding;
import com.example.android.hiittimer.model.Asset;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;
    private MainActivityViewModel mViewModel;

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


    //2かい起動してしまうの修正
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        mViewModel.getDefaultAsset().observe(this, asset -> {
            Timber.i("On get Default Asset");
            if (asset != null) {
                binding.workOutFragment.setVisibility(View.VISIBLE);
                binding.setAsset(asset);
                mViewModel.setAsset(asset);
            } else {
                binding.workOutFragment.setVisibility(View.INVISIBLE);
                Snackbar.make(binding.workOutFragment, "Choose or Create Asset", Snackbar.LENGTH_LONG).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your Asset");

        mViewModel.getAssetList().observe(this, assets -> {
            List<String> list = new ArrayList<>();
            for (Asset asset : assets) {
                list.add(asset.getTitle());
            }

            String[] array = list.toArray(new String[0]);

            builder.setItems(array, (dialog, which) -> {
                dialog.dismiss();
                mViewModel.updateDefaultAsset(true, assets.get(which).getId());
            });
        });

        binding.currentAsset.setOnClickListener(v -> builder.show());

        binding.playWorkout.setOnClickListener(v -> mViewModel.getOpenTimerActivity().setValue(v));
    }


}

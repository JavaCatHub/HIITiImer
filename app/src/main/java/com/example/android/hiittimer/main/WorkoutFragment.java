package com.example.android.hiittimer.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.FragmentWorkoutBinding;
import com.example.android.hiittimer.model.Asset;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;
    private MainActivityViewModel mViewModel;
    private static final String ASSET_KEY = "key";

    public WorkoutFragment() {
    }

    public static WorkoutFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ASSET_KEY, id);
        WorkoutFragment fragment = new WorkoutFragment();
        fragment.setArguments(bundle);

        return fragment;
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
        mViewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);
        if(getArguments() != null){
            mViewModel.getAssetById(getArguments().getInt(ASSET_KEY)).observe(this, new Observer<Asset>() {
                @Override
                public void onChanged(Asset asset) {
                    binding.setAsset(asset);
                }
            });
        }
        binding.playWorkout.setOnClickListener(v -> mViewModel.getOpenTimerActivity().setValue(v));
    }
}

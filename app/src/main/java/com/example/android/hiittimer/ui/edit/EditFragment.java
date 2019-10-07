package com.example.android.hiittimer.ui.edit;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.ValueType;
import com.example.android.hiittimer.databinding.EditFragmentBinding;
import com.example.android.hiittimer.model.Asset;

import java.util.Locale;

public class EditFragment extends Fragment {
    public static final String ARG_EDIT_ASSET_ID = "EDIT_ASSET_ID";

    public static final String ARG_EDIT_KEY = "key";

    private EditViewModel mViewModel;

    private EditFragmentBinding binding;

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(EditViewModel.class);
        binding.setLifecycleOwner(this);
        initViews();
        onClickListener();
        mViewModel.getSaveLiveData().observed(this, view -> saveAsset());
        binding.setViewModel(mViewModel);
    }

    private void showFragmentDialog(ValueType valueType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_timer, null);

        Asset asset;
        if (isCheckAssetNotNull(mViewModel.getAsset().getValue())) {
            asset = mViewModel.getAsset().getValue();
        } else {
            asset = new Asset();
            asset.setDefaultMyself();
        }

        NumberPicker numSec = view.findViewById(R.id.numberPickerSec);
        initNumberPicker(numSec);

        NumberPicker numMin = view.findViewById(R.id.numberPickerMin);
        initNumberPicker(numMin);

        builder.setTitle(R.string.picker_message)
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    int min = numMin.getValue() * 60;
                    int sec = numSec.getValue();
                    if (isCheckAtLeastOneOrMore(min, sec)) {
                        int sum = (min + sec) * 1000;
                        switch (valueType) {
                            case PREPARE: {
                                asset.setPrepare(sum);
                                break;
                            }
                            case WORKOUT: {
                                asset.setWorkOut(sum);
                                break;
                            }
                            case INTERVAL: {
                                asset.setInterval(sum);
                                break;
                            }
                            case COOL_DOWN: {
                                asset.setCoolDown(sum);
                                break;
                            }
                        }
                        mViewModel.setAsset(asset);
                    } else {
                        Toast.makeText(getActivity(), "Please enter at least one second.", Toast.LENGTH_SHORT).show();
                        Timber.d("error value");
                    }

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showPickerDialog(ValueType valueType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_count, null);
        Asset asset;

        if (isCheckAssetNotNull(mViewModel.getAsset().getValue())) {
            asset = mViewModel.getAsset().getValue();
        } else {
            asset = new Asset();
            asset.setDefaultMyself();
        }

        NumberPicker count = view.findViewById(R.id.numberPickerCount);
        count.setMinValue(1);
        count.setMaxValue(60);

        builder.setTitle(R.string.pick_a_count)
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    int x = count.getValue();
                    if (x != 0) {
                        switch (valueType) {
                            case CYCLE: {
                                asset.setCycle(x);
                                break;
                            }
                            case SET: {
                                asset.setSet(x);
                                break;
                            }
                        }
                        mViewModel.setAsset(asset);
                    } else {
                        Toast.makeText(getActivity(), "Please enter at least one or more times.", Toast.LENGTH_SHORT).show();
                        Timber.d("error value");
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void initViews() {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_EDIT_KEY)) {
                mViewModel.setIsNewAsset(true);
                mViewModel.startNewAsset().observe(this,asset ->
                {
                    asset.calculateTotalTime();
                    binding.setAsset(asset);
                });
            } else {
                mViewModel.setIsNewAsset(false);
                mViewModel.setAssetId(getArguments().getInt(ARG_EDIT_ASSET_ID, 0));
                mViewModel.start().observe(this,asset -> {
                    mViewModel.setAsset(asset);
                });
                mViewModel.getAsset().observe(this,asset -> {
                    asset.calculateTotalTime();
                    binding.setAsset(asset);
                });
            }
        }
    }

    private void onClickListener() {
        binding.include.prepareTime.setOnClickListener(v -> showFragmentDialog(ValueType.PREPARE));
        binding.include.workOutTime.setOnClickListener(v -> showFragmentDialog(ValueType.WORKOUT));
        binding.include.intervalTime.setOnClickListener(v -> showFragmentDialog(ValueType.INTERVAL));
        binding.include.coolDownTime.setOnClickListener(v -> showFragmentDialog(ValueType.COOL_DOWN));
        binding.include.setCount.setOnClickListener(v -> showPickerDialog(ValueType.SET));
        binding.include.cycleCount.setOnClickListener(v -> showPickerDialog(ValueType.CYCLE));
    }

    private void initNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMaxValue(59);
        numberPicker.setMinValue(0);
        numberPicker.setFormatter(value -> String.format(Locale.US, "%02d", value));
    }

    private boolean isCheckAtLeastOneOrMore(int min, int sec) {
        return min + sec != 0;
    }

    private boolean isCheckAssetNotNull(Asset asset) {
        return asset != null;
    }

    private void saveAsset() {
        mViewModel.saveAsset(mViewModel.getAsset().getValue());
    }
}

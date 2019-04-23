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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hiittimer.R;
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
        initViews();
        onClickListener();
        mViewModel.getSaveLiveData().observed(this, view -> saveAsset());
    }



    private void showFragmentDialog(TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        NumberPicker numSec = view.findViewById(R.id.numberPickerSec);
        initNumberPicker(numSec);

        NumberPicker numMin = view.findViewById(R.id.numberPickerMin);
        initNumberPicker(numMin);

        builder.setTitle(R.string.picker_message)
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    int min = numMin.getValue() * 60;
                    int sec = numSec.getValue();
                    if (isCheck(min, sec)) {
                        textView.setText(String.valueOf(min + sec));
                    } else {
                        Toast.makeText(getActivity(), "Please enter at least one second.", Toast.LENGTH_SHORT).show();
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
                Asset defaultAsset = new Asset();
                defaultAsset.setDefaultMyself();
                binding.setAsset(defaultAsset);
            } else {
                mViewModel.setIsNewAsset(false);
                mViewModel.setAssetId(getArguments().getInt(ARG_EDIT_ASSET_ID, 0));
                mViewModel.start().observe(this, asset -> binding.setAsset(asset));
            }
        }
    }

    private void onClickListener() {
        binding.include.prepareTime.setOnClickListener(v -> showFragmentDialog((TextView) v));
        binding.include.workOutTime.setOnClickListener(v -> showFragmentDialog((TextView) v));
        binding.include.intervalTime.setOnClickListener(v -> showFragmentDialog((TextView) v));
        binding.include.coolDownTime.setOnClickListener(v -> showFragmentDialog((TextView) v));
    }

    private void initNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMaxValue(59);
        numberPicker.setMinValue(0);
        numberPicker.setFormatter(value -> String.format(Locale.US, "%02d", value));
    }

    private boolean isCheck(int min, int sec) {
        return min + sec != 0;
    }

    private void saveAsset() {
        Asset asset = new Asset();
        asset.setTitle(binding.addAssetTitle.getText().toString());
        asset.setPrepare(parseTextViewToLong(binding.include.prepareTime));
        asset.setWorkOut(parseTextViewToLong(binding.include.workOutTime));
        asset.setInterval(parseTextViewToLong(binding.include.intervalTime));
        asset.setCoolDown(parseTextViewToLong(binding.include.coolDownTime));
        asset.setCycle(parseTextViewToInt(binding.include.cycleCount));
        asset.setSet(parseTextViewToInt(binding.include.setCount));
        asset.setComment(binding.addAssetComment.getText().toString());
        asset.calculateTotalTime();
        mViewModel.saveAsset(asset);
    }

    private long parseTextViewToLong(TextView textView) {
        return Long.parseLong(textView.getText().toString()) * 1000;
    }

    private int parseTextViewToInt(TextView textView) {
        return Integer.parseInt(textView.getText().toString());
    }
}

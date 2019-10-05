package com.example.android.hiittimer.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.FragmentAssetsBinding;
import com.example.android.hiittimer.main.AssetsAdapter;
import com.example.android.hiittimer.main.MainActivityViewModel;
import com.example.android.hiittimer.model.Asset;

import java.math.BigInteger;
import java.util.List;

public class AssetsFragment extends Fragment {

    private MainActivityViewModel mViewModel;
    private AssetsAdapter adapter;
    private FragmentAssetsBinding binding;


    public AssetsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_assets, container, false);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.startLocal().observe(requireActivity(), this::initView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);
        binding.fab.setOnClickListener(v -> mViewModel.addNewAsset());
    }

    private void initView(List<Asset> list) {
        LinearLayoutManager manager = new GridLayoutManager(getActivity(),2);

        adapter = new AssetsAdapter(mViewModel);
        adapter.setAssetList(list);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(manager);
        binding.recycler.setAdapter(adapter);
        Timber.d("This list has %s items.", list.size());
    }

}

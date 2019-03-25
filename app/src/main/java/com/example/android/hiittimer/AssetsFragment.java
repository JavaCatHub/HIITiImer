package com.example.android.hiittimer;

import android.app.Notification;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.hiittimer.databinding.FragmentAssetsBinding;
import com.example.android.hiittimer.model.Asset;

import java.util.List;

public class AssetsFragment extends Fragment {

    private MainActivityViewModel mViewModel;
    private AssetsAdapter adapter;
    private FragmentAssetsBinding binding;
    private static final String TAG = AssetsFragment.class.getSimpleName();


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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_assets,container,false);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.startLocal().observe(this, this::initView);

    }

    private void initView(List<Asset> list){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        adapter = new AssetsAdapter();
        adapter.setAssetList(list);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(manager);
        binding.recycler.setAdapter(adapter);
            Timber.tag(TAG).d("This list has %s items.", list.size());
    }
}

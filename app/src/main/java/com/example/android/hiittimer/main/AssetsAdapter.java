package com.example.android.hiittimer.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.hiittimer.ItemClickListener;
import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.RecyclerAssetsItemBinding;
import com.example.android.hiittimer.model.Asset;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.AssetsAdapterViewHolder> {

    private List<Asset> assetList;
    private MainActivityViewModel viewModel;

    public AssetsAdapter(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public AssetsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.recycler_assets_item;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        RecyclerAssetsItemBinding binding = DataBindingUtil.inflate(layoutInflater, layoutForListItem, parent, false);

        return new AssetsAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetsAdapterViewHolder holder, int position) {
        holder.binding.setAsset(assetList.get(position));
        ItemClickListener listener = asset -> {
            viewModel.getOpenDetailEvent().setValue(asset);
            Timber.d("touched");
        };
        holder.binding.setListener(listener);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public void setAssetList(List<Asset> assetList) {
        this.assetList = assetList;
        notifyDataSetChanged();
    }

    class AssetsAdapterViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerAssetsItemBinding binding;

        AssetsAdapterViewHolder(@NonNull RecyclerAssetsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

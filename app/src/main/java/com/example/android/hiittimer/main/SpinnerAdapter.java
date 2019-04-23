package com.example.android.hiittimer.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.hiittimer.model.Asset;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;

public class SpinnerAdapter extends ArrayAdapter<Asset> {

    private List<Asset> assetList = new ArrayList<>();

    public SpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void setAssetList(List<Asset> list){
        Timber.i("On set Asset list to Spinner Adapter");
        this.assetList = list;
        notifyDataSetChanged();
    }

    public int getAssetListIndex(Asset asset){
        return assetList.indexOf(asset);
    }

    @Override
    public int getCount() {
        return assetList.size();
    }

    @Nullable
    @Override
    public Asset getItem(int position) {
        return assetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return assetList.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position,convertView,parent);
        label.setText(assetList.get(position).getTitle());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position,convertView,parent);
        label.setText(assetList.get(position).getTitle());
        return label;
    }
}

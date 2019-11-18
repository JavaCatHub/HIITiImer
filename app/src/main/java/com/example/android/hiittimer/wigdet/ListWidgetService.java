package com.example.android.hiittimer.wigdet;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.main.MainActivity;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.localdatasource.AppDatabase;

import java.util.List;

import timber.log.Timber;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Timber.i("onGetViewFactory");
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Asset> assetList;
    private Context context;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Timber.i("ListRemoteViewsFactory: onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Timber.i("ListRemoteViewsFactory: onDataSetChanged");
        assetList = AppDatabase.getInstance(context).assetDAO().getExistWidget();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return assetList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Timber.i("ListWidgetService: getViewAt");
        Asset asset = assetList.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        views.setTextViewText(R.id.title, asset.getTitle());
        views.setTextViewText(R.id.total_time, asset.getStringTotalTime());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(MainActivity.ASSET_KEY,asset.getId());
        views.setOnClickFillInIntent(R.id.widget_list_item,fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

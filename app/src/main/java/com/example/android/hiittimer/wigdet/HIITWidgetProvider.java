package com.example.android.hiittimer.wigdet;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.detail.DetailActivity;
import com.example.android.hiittimer.main.MainActivity;
import com.example.android.hiittimer.ui.edit.EditActivity;
import com.example.android.hiittimer.ui.edit.EditFragment;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class HIITWidgetProvider extends AppWidgetProvider {

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int id) {
        Timber.i("onCreate Intent");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setEmptyView(R.id.widget_list_view, R.id.empty);

        Intent intent = new Intent(context, ListWidgetService.class);
        intent.setData(Uri.fromParts("content", intent.toUri(Intent.URI_INTENT_SCHEME), null));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);

        // when click a listView's item, launch detail activity.
//        Intent start = new Intent(context, MainActivity.class);
//        start.putExtra(MainActivity.LAUNCH_DETAIL,true);
//        start.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent startPending = PendingIntent.getActivity(context, 0, start, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setPendingIntentTemplate(R.id.widget_list_view,startPending);

        Intent startDetailActivityIntent = new Intent(context,DetailActivity.class);
        PendingIntent startDetailActivityPendingIntent = PendingIntent.getActivity(context, 0 , startDetailActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view,startDetailActivityPendingIntent);

        // when click a add button, launch edit activity with a new asset.
        Intent addAssetIntent = new Intent(context, EditActivity.class);
        addAssetIntent.putExtra(EditFragment.ARG_EDIT_KEY, true);
        addAssetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent addAssetPendingIntent = PendingIntent.getActivity(context,0,addAssetIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.add_asset_widget,addAssetPendingIntent);

        views.setRemoteAdapter(R.id.widget_list_view, intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.widget_list_view);

        views.setTextViewText(R.id.title, "title");

        appWidgetManager.updateAppWidget(id, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        HIITService.startActionUpdateWidget(context);
    }

    public static void updateHIITWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, HIITWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, HIITWidgetProvider.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName), R.id.widget_list_view);
        }
        super.onReceive(context, intent);
    }
}


package com.example.android.hiittimer.wigdet;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class HIITService extends IntentService {

    public static final String ACTION_UPDATE_HIIT_WIDGET = "com.example.android.HIITtimer.action.update_hiit_widget";

    public HIITService() {
        super("HIITService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            final String action = intent.getAction();
            if(ACTION_UPDATE_HIIT_WIDGET.equals(action)){
                handleActionUpdateHIITWidget();
            }
        }
    }

    private void handleActionUpdateHIITWidget(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, HIITWidgetProvider.class));
        HIITWidgetProvider.updateHIITWidgets(this,appWidgetManager,appWidgetIds);
    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context,HIITService.class);
        intent.setAction(ACTION_UPDATE_HIIT_WIDGET);
        context.startService(intent);
    }
}

/**
 * 
 */
package com.codingevolution.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.codingevolution.flashlight.*;

public class MainWidgetProvider extends AppWidgetProvider {
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

            Intent receiver = new Intent(context, MainWidgetReceiver.class);
            receiver.setAction("com.codingevolution.flashlight.intent.TORCH_TOGGLE");
            receiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.btnSwitchWidget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
}

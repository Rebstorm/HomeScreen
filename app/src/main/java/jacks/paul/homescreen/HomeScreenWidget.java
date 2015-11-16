package jacks.paul.homescreen;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import jacks.paul.homescreen.fragments.HomeFragment;
import jacks.paul.homescreen.fragments.LightFragment;

/**
 * Implementation of App Widget functionality.
 */
public class HomeScreenWidget extends AppWidgetProvider {


    private static final String ONCLICK_ACTION_ICON = "OnClickAction";
    private static final String ONCLICK_ACTION_HUE = "OnClickActionHUE";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_home);

        // Intent for onclick icon
        Intent intent = new Intent(context, HomeScreenWidget.class);
        intent.setAction(ONCLICK_ACTION_ICON);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Intent for onclick hue icon
        Intent intentHue = new Intent(context, HomeScreenWidget.class);
        intentHue.setAction(ONCLICK_ACTION_HUE);
        PendingIntent pendingIntentHue = PendingIntent.getActivity(context, appWidgetId, LightFragment.createLaunchFragmentIntent(context), 0);

        views.setOnClickPendingIntent(R.id.widget_weather_image, pendingIntent);

        views.setOnClickPendingIntent(R.id.hue_widget_icon, pendingIntentHue);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ONCLICK_ACTION_ICON)) {
            try {
                Intent startMainApp = new Intent(context, MainActivity.class);
                startMainApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startMainApp.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(startMainApp);
            }catch(ActivityNotFoundException e){
                Toast.makeText(context,context.getPackageName(), Toast.LENGTH_SHORT).show();
            }

        }else if(intent.getAction().equals(ONCLICK_ACTION_HUE)){
            try{
                Intent startMainApp = new Intent(context, LightFragment.class);
                startMainApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startMainApp.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(startMainApp);
            }catch(ActivityNotFoundException e){
                Toast.makeText(context,context.getPackageName(), Toast.LENGTH_SHORT).show();
            }

        }

    }
}


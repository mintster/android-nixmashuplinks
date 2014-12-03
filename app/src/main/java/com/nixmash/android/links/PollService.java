package com.nixmash.android.links;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PollService extends IntentService {

    private static final String TAG = "PollService";
//    private static final int POLL_INTERVAL = 1000 * 15; // 15 seconds
    private static final int POLL_INTERVAL = 1000 * 60 * 15; // 15 minutes
    public static final String PREF_IS_ALARM_ON = "isAlarmOn";
    public static final String ACTION_SHOW_NOTIFICATION = "com.nixmash.android.links.SHOW_NOTIFICATION";

    public static final String PERM_PRIVATE = "com.nixmash.android.links.PRIVATE";

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() &&
                cm.getActiveNetworkInfo() != null;
        if (!isNetworkAvailable) return;

//        Log.i(TAG, "Received an intent: " + intent);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lastResultId = prefs.getString(LinkFetchr.PREF_LAST_RESULT_ID, null);

        ArrayList<NixMashupLink> items = new LinkFetchr().fetchItems();

        if (items.size() == 0)
            return;

        String resultId = items.get(0).getId();

        if (!resultId.equals(lastResultId)) {
//            Log.i(TAG, "Got a new result: " + resultId);


            Resources r = getResources();
            PendingIntent pi = PendingIntent
                    .getActivity(this, 0, new Intent(this, LinkListActivity.class), 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(r.getString(R.string.new_links_title))
                    .setSmallIcon(R.drawable.ic_nixmashup_notification)
                    .setContentTitle(r.getString(R.string.new_links_title))
                    .setContentText(r.getString(R.string.new_links_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            showBackgroundNotification(0, notification);
        }
//        else {
//            Log.i(TAG, "Got an old result: " + resultId);
//        }

        prefs.edit()
                .putString(LinkFetchr.PREF_LAST_RESULT_ID, resultId)
                .apply();

    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC,
                    System.currentTimeMillis(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PollService.PREF_IS_ALARM_ON, isOn)
                .commit();
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra("REQUEST_CODE", requestCode);
        i.putExtra("NOTIFICATION", notification);

        sendOrderedBroadcast(i, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }


}



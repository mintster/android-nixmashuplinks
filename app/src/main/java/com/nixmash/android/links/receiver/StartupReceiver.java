package com.nixmash.android.links.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nixmash.android.links.service.PollService;

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "NixMashupLinksStartupReceiver";

    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        This method is called when the BroadcastReceiver is receiving an Intent broadcast.
//        Log.i(TAG, "Received broadcast intent: " + intent.getAction());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isOn = prefs.getBoolean(PollService.PREF_IS_ALARM_ON, false);
        PollService.setServiceAlarm(context, isOn);
    }
}

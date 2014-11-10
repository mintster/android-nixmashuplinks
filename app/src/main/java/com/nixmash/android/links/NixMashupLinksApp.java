package com.nixmash.android.links;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import java.io.IOException;

public class NixMashupLinksApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        clearQueries();
//        try {
//            Installation.deleteInstallationFile(mContext);
//        } catch (IOException e) {
//        }
    }

    public static Context getContext() {
        return mContext;
    }

    private void clearQueries() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(LinkFetchr.PREF_TAG_QUERY, null)
                .putString(LinkFetchr.PREF_SEARCH_QUERY, null)
                .commit();
    }
}
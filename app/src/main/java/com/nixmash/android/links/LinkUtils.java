package com.nixmash.android.links;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by daveburke on 10/27/14.
 */
public class LinkUtils {

    public static final String ALL_RECORDS = "ALL";

    public static Boolean isInSmallPortraitMode(Context _context) {
        Boolean _isInSmallPortraitMode = false;
        int _orientation = _context.getResources().getConfiguration().orientation;
        int _screenSize = _context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        if (_orientation == Configuration.ORIENTATION_PORTRAIT &&
                _screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            _isInSmallPortraitMode = true;

        return _isInSmallPortraitMode;
    }

    public static Boolean isInSmallMode(Context _context) {
        Boolean _isInSmallMode = false;
        int _screenSize = _context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        if (_screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            _isInSmallMode = true;

        return _isInSmallMode;
    }


    public static Boolean isMessageRecord(String _sDate) {
        Boolean _isMessageRecord = false;
        DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        java.util.Date _date = null;
        try {
            _date = dateFormat.parse(_sDate);
        } catch (ParseException e) {
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(_date);
        int year = cal.get(Calendar.YEAR);
        if (year == 1980)
            _isMessageRecord = true;

        return _isMessageRecord;
    }

    public static void prepareCategorySearch(Activity activity, String search) {
        if (search.toUpperCase().equals(ALL_RECORDS))
            search = null;

        PreferenceManager.getDefaultSharedPreferences(activity)
                .edit()
                .putString(LinkFetchr.PREF_TAG_QUERY, null)
                .putString(LinkFetchr.PREF_SEARCH_QUERY, search)
                .commit();

    }

    public static void prepareTagSearch(Activity activity, String tag) {
        PreferenceManager.getDefaultSharedPreferences(activity)
                .edit()
                .putString(LinkFetchr.PREF_TAG_QUERY, StringUtils.lowerCase(tag))
                .putString(LinkFetchr.PREF_SEARCH_QUERY, null)
                .commit();
    }

    public static void resetCategoryFocus() {
        LinkListActivity.tagPosition = 0;
    }


    public static void startLinkListActivity(Activity activity) {
        LinkUtils.resetCategoryFocus();
        prepareCategorySearch(activity, ALL_RECORDS);
        Intent linkListIntent = new Intent(activity, LinkListActivity.class);
        linkListIntent.putExtra(SingleFragmentActivity.EXTRA_TAG, 0);

        activity.startActivity(linkListIntent);
    }

    public static Boolean isPhoneScreenType(Context _context) {
        Boolean _isPhoneScreenType = true;
        if (_context.getResources().getString(R.string.screen_type) != SingleFragmentActivity.PHONE_SCREEN_TYPE)
            _isPhoneScreenType = false;
        return _isPhoneScreenType;
    }
}

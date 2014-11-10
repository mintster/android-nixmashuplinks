package com.nixmash.android.links;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by daveburke on 10/27/14.
 */
public class LinkUtils {

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

}

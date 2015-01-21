package com.nixmash.android.links.io;


import java.util.ArrayList;

import android.content.Context;

import com.nixmash.android.links.io.model.NixMashupLink;

public class Links {
    private static final String TAG = "LinksHandler";
    private static final String FILENAME = "links.json";

    private ArrayList<NixMashupLink> mLinks;
    private LinkJSONSerializer mSerializer;

    private Context mAppContext;
    private static Links sLinks;

    private Links(Context appContext) {
        mAppContext = appContext;
        mSerializer = new LinkJSONSerializer(mAppContext, FILENAME);

        try {
            mLinks = mSerializer.loadLinks();
        } catch (Exception e) {
            mLinks = new ArrayList<NixMashupLink>();
//            Log.e(TAG, "Error loading links: ", e);
        }
    }

    public static Links get(Context c) {
        if (sLinks == null) {
            sLinks = new Links(c.getApplicationContext());
        }
        return sLinks;
    }

    public NixMashupLink getLink(String id) {
        for (NixMashupLink c : mLinks) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

//    public void addCrime(Crime c) {
//        mCrimes.add(c);
//        saveCrimes();
//    }

    public ArrayList<NixMashupLink> getLinks() {
        return mLinks;
    }

    public boolean saveLinks(ArrayList<NixMashupLink> links) {
        try {
            mSerializer.saveLinks(links);
            mLinks = links;
//            Log.d(TAG, "links saved to file");
            return true;
        } catch (Exception e) {
//            Log.e(TAG, "Error saving links: " + e);
            return false;
        }
    }

}


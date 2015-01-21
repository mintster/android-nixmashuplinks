package com.nixmash.android.links.io;

/**
 * Created by daveburke on 9/17/14.
 */

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.nixmash.android.links.io.LinkFetchr;
import com.nixmash.android.links.io.model.NixMashupLink;

/**
 * Created by daveburke on 9/8/14.
 */
public class LinkDownloader<Token> extends HandlerThread {

    private static final String TAG = "LinkDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    Handler mHandler;
    Map<Token, NixMashupLink> requestMap =
            Collections.synchronizedMap(new HashMap<Token, NixMashupLink>());
    Handler mResponseHandler;
    Listener<Token> mListener;

    public interface Listener<Token> {
        void onLinkDownloaded(Token token, NixMashupLink link, Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
//                    Log.i(TAG, "Got a request for url: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    public LinkDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void queueLink(Token token, NixMashupLink link) {
//        Log.i(TAG, "Got a URL: " + link.getThumbnailUrl());
        requestMap.put(token, link);

        mHandler
                .obtainMessage(MESSAGE_DOWNLOAD, token)
                .sendToTarget();
    }

    private void handleRequest(final Token token) {
        final NixMashupLink link = requestMap.get(token);
        if (link == null)
            return;

        try {
            byte[] bitmapBytes = new LinkFetchr().getUrlBytes(link.getThumbnailUrl());
            final Bitmap thumbnail = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
//            Log.i(TAG, "Bitmap created");

            mResponseHandler.post(new Runnable() {
                public void run() {
                    if (requestMap.get(token) != link)
                        return;

                    requestMap.remove(token);
                    mListener.onLinkDownloaded(token, link, thumbnail);
                }
            });
        } catch (IOException ioe) {
//            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

}

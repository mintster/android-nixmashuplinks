package com.nixmash.android.links;

import android.net.Uri;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LinkFetchr {

    public static final String TAG = "LinkFetchr";
    public static final String PREF_SEARCH_QUERY = "searchQuery";
    public static final String PREF_TAG_QUERY = "tagQuery";
    public static final String PREF_LAST_RESULT_ID = "lastresultId";

    //    private static final String ENDPOINT = "http://api.nixmashuplinks.com/rest/links/xml/";
    private static final String XML_ENDPOINT = NixMashupLinksApp.getContext().getResources().getString(R.string.api_xml_url);
    private static final String IMAGES_ENDPOINT = NixMashupLinksApp.getContext().getResources().getString(R.string.api_images_url);
    private static final String API_KEY = NixMashupLinksApp.getContext().getResources().getString(R.string.api_key);

    private static final String PARAM_TEXT = "query";
    private static final String XML_LINK = "nixmashuplink";
    private static final String XML_ID = "id";
    private static final String XML_TITLE = "title";
    private static final String XML_LINKURL = "linkurl";
    private static final String XML_LINKTEXT = "linktext";
    private static final String XML_TAGS = "tags";
    private static final String XML_POSTDATE = "postdate";
    private static final String XML_SEASON = "season";
    private static final String XML_LINKIMAGE = "linkimage";

    private static final String DEVICE_GUID = Installation.deviceId(NixMashupLinksApp.getContext());

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public ArrayList<NixMashupLink> downloadLinks(String url) {
        ArrayList<NixMashupLink> links = new ArrayList<NixMashupLink>();
//        Log.i(TAG, "DEVICE GUID: " + DEVICE_GUID);

        try {

            String xmlString = getUrl(url);
//            Log.i(TAG, "Received xml: " + xmlString);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            parseItems(links, parser);
        } catch (IOException ioe) {
//            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (XmlPullParserException xppe) {
//            Log.e(TAG, "Failed to parse items", xppe);
        }
        return links;
    }

    public ArrayList<NixMashupLink> search(String query) {
        String url = Uri.parse(XML_ENDPOINT).buildUpon()
                .appendPath("search")
                .appendQueryParameter(PARAM_TEXT, query)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("device_id", DEVICE_GUID)
                .build().toString();
//        Log.i(TAG, url);
        return downloadLinks(url);

    }

    public ArrayList<NixMashupLink> fetchByTag(String tag) {
        String url = Uri.parse(XML_ENDPOINT).buildUpon()
                .appendPath("tag")
                .appendPath(tag)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("device_id", DEVICE_GUID)
                .build().toString();
//        Log.i(TAG, url);
        return downloadLinks(url);

    }

    public ArrayList<NixMashupLink> fetchItems() {
        String url = Uri.parse(XML_ENDPOINT).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("device_id", DEVICE_GUID)
                .build().toString();
//        Log.i(TAG, url);
        return downloadLinks(url);
    }


    void parseItems(ArrayList<NixMashupLink> links, XmlPullParser parser) throws XmlPullParserException, IOException {

        NixMashupLink link = null;
        String tagName;
        int eventType = parser.getEventType();
        // process tag while not reaching the end of document
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // at start of document: START_DOCUMENT
                case XmlPullParser.START_DOCUMENT:
                    break;

                // at start of a tag: START_TAG
                case XmlPullParser.START_TAG:
                    // get tag name
                    tagName = parser.getName();

                    if (tagName.equalsIgnoreCase(XML_LINK)) {
                        link = new NixMashupLink();
                    } else if (tagName.equalsIgnoreCase(XML_ID)) {
                        link.setId(parser.nextText());
                    } else if (tagName.equalsIgnoreCase(XML_TITLE)) {
                        link.setTitle(parser.nextText());
                    } else if (tagName.equalsIgnoreCase(XML_LINKTEXT)) {
                        link.setLinkText(parser.nextText());
                    } else if (tagName.equalsIgnoreCase(XML_LINKURL)) {
                        link.setLinkUrl(parser.nextText());
                    } else if (tagName.equalsIgnoreCase(XML_POSTDATE)) {
                        link.setPostDate(parser.nextText());
                    } else if (tagName.equalsIgnoreCase(XML_TAGS)) {
                        link.setTags(parser.nextText());
                    } else if (tagName.equalsIgnoreCase(XML_SEASON)) {
                        link.setSeason(parser.nextText());
                    } else if (tagName.equalsIgnoreCase(XML_LINKIMAGE)) {
                        link.setLinkImage(parser.nextText());
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if (tagName.equalsIgnoreCase(XML_LINK)) {
//                        String thumbnailUrl = imageUrl(
//                                String.format("%s/%s/zThumb_%s", IMAGES_ENDPOINT, link.getSeason(), link.getLinkImage());
//                        String imageUrl =
//                                String.format("%s/%s/zOpt_%s", IMAGES_ENDPOINT, link.getSeason(), link.getLinkImage());
//                        String maxImageUrl =
//                                String.format("%s/%s/%s", IMAGES_ENDPOINT, link.getSeason(), link.getLinkImage());

                        link.setThumbnailUrl(imageUrl(link, "zThumb_"));
                        link.setImageUrl(imageUrl(link, "zOpt_"));
                        link.setMaxImageUrl(imageUrl(link, StringUtils.EMPTY));
                        links.add(link);
                    }
                    break;

            }
            eventType = parser.next();

        }
    }

    private String imageUrl(NixMashupLink link, String prefix) {
        return String.format("%s/%s/%s%s", IMAGES_ENDPOINT, link.getSeason(), prefix, link.getLinkImage());

    }
}

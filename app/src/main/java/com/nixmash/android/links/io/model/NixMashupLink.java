package com.nixmash.android.links.io.model;

import org.json.JSONException;
import org.json.JSONObject;

public class NixMashupLink {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_LINKURL = "url";
    private static final String JSON_LINKTEXT = "owner";
    private static final String JSON_THUMBNAILURL = "thumbnail_url";
    private static final String JSON_IMAGEURL = "image_url";
    private static final String JSON_MAXIMAGEURL = "max_image_url";
    private static final String JSON_POSTDATE = "postdate";
    private static final String JSON_TAGS = "tags";
    private static final String JSON_SEASON = "season";
    private static final String JSON_LINKIMAGE = "linkimage";

    private String mId;
    private String mTitle;
    private String mLinkUrl;
    private String mLinkText;
    private String mThumbnailUrl;
    private String mImageUrl;
    private String mMaxImageUrl;
    private String mPostDate;
    private String mTags;


    private String mSeason;
    private String mLinkImage;

    public String getTags() {
        return mTags;
    }
    public void setTags(String tags) {
        mTags = tags;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getMaxImageUrl() {
        return mMaxImageUrl;
    }
    public void setMaxImageUrl(String imageUrl) {
        mMaxImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public String getId() {
        return mId;
    }
    public void setId(String id) {
        mId = id;
    }

    public String getLinkUrl() {
        return mLinkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        mLinkUrl = linkUrl;
    }

    public String getLinkText() {
        return mLinkText;
    }
    public void setLinkText(String linkText) {
        mLinkText = linkText;
    }

    public String getPostDate() {
        return mPostDate;
    }
    public void setPostDate(String postDate) {
        mPostDate = postDate;
    }

    public String getSeason() {
        return mSeason;
    }
    public void setSeason(String season) {
        mSeason = season;
    }

    public String getLinkImage() {
        return mLinkImage;
    }
    public void setLinkImage(String linkImage) {
        mLinkImage = linkImage;
    }

    public String toString() {
        return mTitle;
    }

    public NixMashupLink() {}

    public NixMashupLink(JSONObject json) throws JSONException {
        mId = json.getString(JSON_ID);
        mTitle = json.getString(JSON_TITLE);
        mLinkUrl = json.getString(JSON_LINKURL);
        mLinkText = json.getString(JSON_LINKTEXT);
        mThumbnailUrl = json.getString(JSON_THUMBNAILURL);
        mImageUrl = json.getString(JSON_IMAGEURL);
        mMaxImageUrl = json.getString(JSON_MAXIMAGEURL);
        mPostDate = json.getString(JSON_POSTDATE);
        mTags = json.getString(JSON_TAGS);
        mSeason = json.getString(JSON_SEASON);
        mLinkImage = json.getString(JSON_LINKIMAGE);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_LINKURL, mLinkUrl);
        json.put(JSON_LINKTEXT, mLinkText);
        json.put(JSON_THUMBNAILURL, mThumbnailUrl);
        json.put(JSON_IMAGEURL, mImageUrl);
        json.put(JSON_MAXIMAGEURL, mMaxImageUrl);
        json.put(JSON_POSTDATE, mPostDate);
        json.put(JSON_TAGS, mTags);
        json.put(JSON_SEASON, mSeason);
        json.put(JSON_LINKIMAGE, mLinkImage);
        return json;
    }

}
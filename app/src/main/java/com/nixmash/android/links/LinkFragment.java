package com.nixmash.android.links;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class LinkFragment extends VisibleFragment {
    public static final String EXTRA_LINK_ID = "nixmashuplinks.LINK_ID";
    public static final String EXTRA_REFRESH_LINKS = "nixmashuplinks.REFRESH_LINKS";
    private static final String TAG = "LinkFragment";
    private static final int SMALL_PORTRAIT_TAGS_WIDTH = 28;

    NixMashupLink mLink;
    ImageView mImageView;
    TextView mTitleView;
    TextView mBodyView;
    TextView mPostDateView;

    Bitmap mImage;
    Callbacks mCallbacks;
    String[] mTags;
    Context listItemContext;

    // region Callbacks

    public interface Callbacks {
        void onLinkUpdated(NixMashupLink link);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    // endregion

    // region newInstance()

    public static LinkFragment newInstance(String linkId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LINK_ID, linkId);

        LinkFragment fragment = new LinkFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // endregion

    // region override methods


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String linkId = (String) getArguments().getSerializable(EXTRA_LINK_ID);
        mLink = Links.get(getActivity()).getLink(linkId);

        Intent i = new Intent(getActivity(), LinkListActivity.class);
        i.putExtra(LinkFragment.EXTRA_REFRESH_LINKS, "1");

        mTags = new String[]{};
        setHasOptionsMenu(true);
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_link, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mImageView = (ImageView) v.findViewById(R.id.details_imageView);
        new LoadImage().execute(mLink.getImageUrl());

        mTitleView = (TextView) v.findViewById(R.id.details_id_titleTextView);
        mTitleView.setText(mLink.getTitle());

        mPostDateView = (TextView) v.findViewById(R.id.details_id_postDateTextView);
        mPostDateView.setText(getResources().getString(R.string.published_on_text) + " " + mLink.getPostDate());

        mBodyView = (TextView) v.findViewById(R.id.details_id_bodyTextView);
        mBodyView.setText(mLink.getLinkText());

        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri linkPageUri = Uri.parse(mLink.getLinkUrl());
                Intent i = new Intent(getActivity(), WebPageActivity.class);
                i.setData(linkPageUri);
                startActivity(i);
            }
        });

        listItemContext = v.getContext();
        mTags = StringUtils.split(mLink.getTags(), "|");

        int _widthSoFar;
        int _smallPortraitWidth;
        int _tagWidth;

        LinearLayout contentView = (LinearLayout) v.findViewById(R.id.details_tags_layout);
        contentView.removeAllViews();
        LinearLayout tagsView = new LinearLayout(listItemContext);

        _smallPortraitWidth = SMALL_PORTRAIT_TAGS_WIDTH;
        _widthSoFar = 0;

        for (final String tag : mTags) {

            _tagWidth = tag.length();
            _widthSoFar += _tagWidth;

            if ((_widthSoFar < _smallPortraitWidth && LinkUtils.isInSmallMode(listItemContext)) ||
                    !LinkUtils.isInSmallMode(listItemContext)) {

                final TextView tagView = new TextView(listItemContext);
                tagView.setText(StringUtils.capitalize(tag) + " ");
                tagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceManager.getDefaultSharedPreferences(getActivity())
                                .edit()
                                .putString(LinkFetchr.PREF_TAG_QUERY, tag)
                                .putString(LinkFetchr.PREF_SEARCH_QUERY, null)
                                .commit();

                        Intent i = new Intent(getActivity(), LinkListActivity.class);
                        startActivity(i);
                    }
                });


                tagsView.addView(tagView);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tagView.getLayoutParams();
                params.setMargins(0, 0, 16, 0); // left, top, right, bottom
                tagView.setLayoutParams(params);
                tagView.setTextAppearance(getActivity(), R.style.DetailLinkTag);
            }
        }
        contentView.addView(tagsView);

        return v;
    }


    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        protected Bitmap doInBackground(String... args) {
            try {
                mImage = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mImage;
        }

        protected void onPostExecute(Bitmap image) {
            mImageView.setImageBitmap(image);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getActivity(), LinkListActivity.class);
                i.putExtra(LinkFragment.EXTRA_REFRESH_LINKS, "0");
//                NavUtils.navigateUpFromSameTask(getActivity());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // endregion
}

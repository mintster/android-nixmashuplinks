package com.nixmash.android.links;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.URL;

public class LinkFragment extends VisibleFragment {
    public static final String EXTRA_LINK_ID = "nixmashuplinks.LINK_ID";
    public static final String EXTRA_LINK_URL = "nixmashuplinks.LINK_URL";
    public static final String EXTRA_REFRESH_LINKS = "nixmashuplinks.REFRESH_LINKS";
    private static final String TAG = "LinkFragment";
    private static final int SMALL_PORTRAIT_TAGS_WIDTH = 40;
    private static final int REQUEST_SEARCH = 0;
    private static final String DIALOG_SEARCH = "search";

    NixMashupLink mLink;
    ImageView mImageView;
    TextView mTitleView;
    TextView mBodyView;
    TextView mPostDateView;

    Bitmap mImage;
    Callbacks mCallbacks;
    String[] mTags;
    Context linkContext;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String linkId = (String) getArguments().getSerializable(EXTRA_LINK_ID);
        mLink = Links.get(getActivity()).getLink(linkId);

//        Intent i = new Intent(getActivity(), MainActivity.class);
//        i.putExtra(LinkFragment.EXTRA_REFRESH_LINKS, "1");

        mTags = new String[]{};
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_link, parent, false);

        mImageView = (ImageView) v.findViewById(R.id.details_imageView);
        linkContext = v.getContext();
        if (LinkUtils.isPhoneScreenType(linkContext))
            new LoadImage().execute(mLink.getImageUrl());
        else
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
//                Uri linkPageUri = Uri.parse(mLink.getLinkUrl());
                Intent i = new Intent(getActivity(), WebPageActivity.class);
                i.putExtra(LinkFragment.EXTRA_LINK_ID, mLink.getId());
                i.putExtra(LinkFragment.EXTRA_LINK_URL, mLink.getLinkUrl());
//                i.setData(linkPageUri);
                startActivity(i);
            }
        });

        mTags = StringUtils.split(mLink.getTags(), "|");

        int _availableWidth;
        int _smallPortraitWidth;
        int _tagWidth;

        LinearLayout contentView = (LinearLayout) v.findViewById(R.id.details_tags_layout);
        contentView.removeAllViews();
        LinearLayout tagsView = new LinearLayout(linkContext);

        _smallPortraitWidth = SMALL_PORTRAIT_TAGS_WIDTH;
        _availableWidth = _smallPortraitWidth;

        for (final String tag : mTags) {

            _tagWidth = tag.length();
            _availableWidth = _availableWidth - _tagWidth;

            if (_availableWidth > _tagWidth || !LinkUtils.isInPortraitMode(linkContext))
            {

                final TextView tagView = new TextView(linkContext);
                tagView.setText(StringUtils.capitalize(tag) + " ");
                tagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceManager.getDefaultSharedPreferences(getActivity())
                                .edit()
                                .putString(LinkFetchr.PREF_TAG_QUERY, tag)
                                .putString(LinkFetchr.PREF_SEARCH_QUERY, null)
                                .commit();

                        Intent i = new Intent(getActivity(), MainActivity.class);
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
                mImage = BitmapFactory.decodeStream((InputStream) new
                                                URL(args[0]).getContent());
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
        if (requestCode == REQUEST_SEARCH) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                LinkUtils.resetCategoryFocus();
                FragmentManager fm = getActivity()
                        .getFragmentManager();
                SearchFragment dialog = SearchFragment
                        .newInstance();
                dialog.setTargetFragment(LinkFragment.this, REQUEST_SEARCH);
                dialog.show(fm, DIALOG_SEARCH);
                return true;
            case R.id.menu_item_clear:
                LinkUtils.startMainActivity(getActivity());
                return true;
            case R.id.menu_item_refresh:
                LinkUtils.startMainActivity(getActivity());
                return true;
            case R.id.menu_item_about:
                LinkUtils.resetCategoryFocus();
                Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    getActivity().invalidateOptionsMenu();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // endregion
}

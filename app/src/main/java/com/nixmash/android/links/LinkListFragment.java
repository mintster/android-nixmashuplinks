package com.nixmash.android.links;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class LinkListFragment extends ListFragment {

    // region Properties

    private static final String TAG = "LinkListFragment";
    private static final int SMALL_PORTRAIT_TAGS_WIDTH = 40;
    private ArrayList<NixMashupLink> mLinks;
    private String[] mTags;
    private Callbacks mCallbacks;
    private ListView mListView;
    private Context listItemContext;
    private LinkDownloader<View> mLinkDownloadThread;
    private static final int REQUEST_SEARCH = 0;
    private static final String DIALOG_SEARCH = "search";

    // endregion

    public interface Callbacks {
        void onLinkSelected(NixMashupLink nixMashupLink);
    }

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            if we receive this, we're visible, so cancel
//            the notification
//            Log.i(TAG, "canceling notification");

            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mOnShowNotification);
    }

    public void updateItems() {
        updateItems(false);
    }

    public void updateItems(Boolean blnClearQuery) {
        if (blnClearQuery) {
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putString(LinkFetchr.PREF_SEARCH_QUERY, null)
                    .putString(LinkFetchr.PREF_TAG_QUERY, null)
                    .commit();
        }
        new FetchItemsTask().execute();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLinkDownloadThread.quit();
//        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLinkDownloadThread.clearQueue();
    }

    public static Fragment newInstance(int position) {
        Fragment fragment = new LinkListFragment();
        Bundle args = new Bundle();
        args.putInt(BaseActivity.EXTRA_CATEGORY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.app_name);
        updateItems();

        mTags = new String[]{};

        mLinkDownloadThread = new LinkDownloader<View>(new Handler());
        mLinkDownloadThread.setListener(new LinkDownloader.Listener<View>() {
            public void onLinkDownloaded(View view, NixMashupLink link, Bitmap thumbnail) {
                if (isVisible()) {

                    ImageView imageView = (ImageView) view.findViewById(R.id.list_item_imageView);
                    TextView titleTextView = (TextView) view.findViewById(R.id.list_item_title_textView);

                    Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
                    titleTextView.setTypeface(typeface);

                    titleTextView.setText(link.getTitle());
                    imageView.setImageBitmap(thumbnail);

                    if (!LinkUtils.isMessageRecord(link.getPostDate())) {
                        TextView detailsTextView = (TextView) view.findViewById(R.id.list_item_details_linkBodyView);
                        TextView postDateTextView = (TextView) view.findViewById(R.id.list_item_details_postDateTextView);

                        String _postDate = getResources().getString(R.string.published_on_text) + " " + link.getPostDate();
                        TextView listPostDateTextView = (TextView) view.findViewById(R.id.list_item_postDate_textView);

                        final NixMashupLink fLink = link;
                        Boolean isSmallDevice = true;
                        if (detailsTextView != null) {
                            // running on tablet
                            isSmallDevice = false;
                            detailsTextView.setText(link.getLinkText());
                            postDateTextView.setText(_postDate);
                        } else {
                            listPostDateTextView.setText(_postDate);
                        }

                        mTags = StringUtils.split(link.getTags(), "|");
                        listItemContext = view.getContext();

                        int _availableWidth;
                        int _smallPortraitWidth;
                        int _tagWidth;

                        LinearLayout contentView = (LinearLayout) view.findViewById(R.id.tags_layout);
                        contentView.removeAllViews();
                        LinearLayout tagsView = new LinearLayout(listItemContext);

                        _smallPortraitWidth = SMALL_PORTRAIT_TAGS_WIDTH;
                        _availableWidth = _smallPortraitWidth;

                        for (final String tag : mTags) {

                            _tagWidth = tag.length();
                            _availableWidth = _availableWidth - _tagWidth;

//                            if ((_availableWidth > _tagWidth && isSmallDevice)
//                                    || Configuration.ORIENTATION_PORTRAIT) {

                            if (_availableWidth > _tagWidth || !LinkUtils.isInPortraitMode(listItemContext))
                            {
                                final TextView tagView = new TextView(listItemContext);
                                tagView.setText(StringUtils.capitalize(tag) + " ");
                                tagsView.addView(tagView);

                                int _rightMargin = 7;
                                if (!LinkUtils.isInSmallPortraitMode(listItemContext)) {
                                    if (LinkUtils.isInSmallMode(listItemContext))
                                        tagView.setTextAppearance(getActivity(), R.style.ListLinkTag);
                                    else
                                        tagView.setTextAppearance(getActivity(), R.style.ListDetailLinkTag);
                                    _rightMargin = 11;
                                } else
                                    tagView.setTextAppearance(getActivity(), R.style.ListLinkTag);

                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tagView.getLayoutParams();
                                params.setMargins(0, 0, _rightMargin, 0); // left, top, right, bottom
                                tagView.setLayoutParams(params);


                            }
                        }

                        contentView.addView(tagsView);
                    }
                }
            }
        });

        mLinkDownloadThread.start();
        mLinkDownloadThread.getLooper();
//        Log.i(TAG, "Background thread started");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, parent, false);
        mListView = (ListView) v.findViewById(android.R.id.list);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        NixMashupLink c = mLinks.get(position);
        mCallbacks.onLinkSelected(c);
    }

    private class FetchItemsTask extends
            AsyncTask<Void, Void, ArrayList<NixMashupLink>> {
        @Override
        protected ArrayList<NixMashupLink> doInBackground(Void... params) {
            Activity activity = getActivity();
            if (activity == null)
                return new ArrayList<NixMashupLink>();

            String query = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(LinkFetchr.PREF_SEARCH_QUERY, null);

            String tag = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(LinkFetchr.PREF_TAG_QUERY, null);

            if (query != null) {
                return new LinkFetchr().search(query);
            } else if (tag != null) {
                return new LinkFetchr().fetchByTag(tag);
            } else {
                return new LinkFetchr().fetchItems();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<NixMashupLink> items) {
            mLinks = items;
            Links.get(NixMashupLinksApp.getContext()).saveLinks(mLinks);
            setupAdapter();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
                dialog.setTargetFragment(LinkListFragment.this, REQUEST_SEARCH);
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
                Intent i = new Intent(getActivity(), AboutActivity.class);
                startActivity(i);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    void setupAdapter() {
        if (getActivity() == null || mListView == null) return;
        if (mLinks != null) {
            mListView.setAdapter(new LinkAdapter(mLinks));
        } else {
            mListView.setAdapter(null);
        }
    }

    private class LinkAdapter extends ArrayAdapter<NixMashupLink> {
        public LinkAdapter(ArrayList<NixMashupLink> links) {
            super(getActivity(), 0, links);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.fragment_list_item_link, parent, false);
            }

            NixMashupLink link = getItem(position);
            mLinkDownloadThread.queueLink(convertView, link);

            return convertView;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_SEARCH) {
            updateItems();
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
}
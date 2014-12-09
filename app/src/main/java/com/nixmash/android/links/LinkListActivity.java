package com.nixmash.android.links;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class LinkListActivity extends SingleFragmentActivity
        implements LinkListFragment.Callbacks {

    private static final String TAG = "LinkListActivity";
    public static int tagPosition = 0;

    @Override
    public Fragment createFragment() {
        return new LinkListFragment();
    }

    @Override
    public void onLinkSelected(NixMashupLink link) {
        if (!LinkUtils.isMessageRecord(link.getPostDate())) {
                Intent i = new Intent(this, LinkPagerActivity.class);
                i.putExtra(LinkFragment.EXTRA_LINK_ID, link.getId());
                i.putExtra(SingleFragmentActivity.EXTRA_TAG, tagPosition);
                startActivityForResult(i, 0);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LinkListFragment fragment =
                (LinkListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
        LinkUtils.prepareCategorySearch(this, LinkUtils.ALL_RECORDS);
            fragment.updateItems();
    }


}

package com.nixmash.android.links;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

public class LinkListActivity extends SingleFragmentActivity
        implements LinkListFragment.Callbacks {

    private static final String TAG = "LinkListActivity";

    @Override
    public Fragment createFragment() {
        return new LinkListFragment();
    }

    @Override
    public void onLinkSelected(NixMashupLink link) {
        if (!LinkUtils.isMessageRecord(link.getPostDate())) {
            if (findViewById(R.id.list_item_details_linkBodyView) == null) {
                Intent i = new Intent(this, LinkPagerActivity.class);
                i.putExtra(LinkFragment.EXTRA_LINK_ID, link.getId());
                startActivityForResult(i, 0);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        LinkListFragment fragment = (LinkListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
            fragment.updateItems();
    }
}

package com.nixmash.android.links.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import com.nixmash.android.links.util.LinkUtils;
import com.nixmash.android.links.io.model.NixMashupLink;
import com.nixmash.android.links.R;

public class MainActivity extends BaseActivity implements LinkListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.contentFrameContainer);

        if (fragment == null) {
            fragment = LinkListFragment.newInstance(categoryPosition);
            manager.beginTransaction()
                    .add(R.id.contentFrameContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intent.putExtra(BaseActivity.EXTRA_CATEGORY_POSITION, categoryPosition);
        LinkListFragment fragment =
                (LinkListFragment) getFragmentManager()
                        .findFragmentById(R.id.contentFrameContainer);
        LinkUtils.prepareCategorySearch(this,
                getResources().getStringArray(R.array.category_search_array)[categoryPosition]);
        fragment.updateItems();
    }

    @Override
    public void onLinkSelected(NixMashupLink nixMashupLink) {
        if (!LinkUtils.isMessageRecord(nixMashupLink.getPostDate())) {
            Intent i = new Intent(this, LinkPagerActivity.class);
            i.putExtra(LinkFragment.EXTRA_LINK_ID, nixMashupLink.getId());
            i.putExtra(EXTRA_CATEGORY_POSITION, categoryPosition);
            startActivityForResult(i, 0);
        }
    }
}
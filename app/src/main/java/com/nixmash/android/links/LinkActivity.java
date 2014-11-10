package com.nixmash.android.links;


import java.util.UUID;

import android.support.v4.app.Fragment;

public class LinkActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String linkId = (String)getIntent()
                .getSerializableExtra(LinkFragment.EXTRA_LINK_ID);
        return LinkFragment.newInstance(linkId);
    }
}

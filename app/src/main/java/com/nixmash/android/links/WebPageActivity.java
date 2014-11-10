package com.nixmash.android.links;

import android.support.v4.app.Fragment;

public class WebPageActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new WebPageFragment();
    }
}

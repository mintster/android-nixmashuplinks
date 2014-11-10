package com.nixmash.android.links;



import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;

public class LinkPagerActivity extends FragmentActivity implements LinkFragment.Callbacks {
    ViewPager mViewPager;

    public void onLinkUpdated(NixMashupLink link) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        final ArrayList<NixMashupLink> links = Links.get(this).getLinks();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return links.size();
            }
            @Override
            public Fragment getItem(int pos) {
                String linkId =  links.get(pos).getId();
                return LinkFragment.newInstance(linkId);
            }
        });

        String linkId = (String)getIntent().getSerializableExtra(LinkFragment.EXTRA_LINK_ID);
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getId().equals(linkId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

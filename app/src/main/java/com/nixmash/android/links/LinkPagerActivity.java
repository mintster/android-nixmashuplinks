package com.nixmash.android.links;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;

import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class LinkPagerActivity extends BaseActivity
        implements LinkFragment.Callbacks {

    ViewPager mViewPager;

    public void onLinkUpdated(NixMashupLink link) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_pager);
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        final ArrayList<NixMashupLink> links = Links.get(this).getLinks();

        FragmentManager fm = getFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return links.size();
            }

            @Override
            public Fragment getItem(int pos) {
                String linkId = links.get(pos).getId();
                return LinkFragment.newInstance(linkId);
            }
        });

        String linkId = (String) getIntent().getSerializableExtra(LinkFragment.EXTRA_LINK_ID);
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getId().equals(linkId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }


}

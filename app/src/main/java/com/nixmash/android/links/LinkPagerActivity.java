package com.nixmash.android.links;


import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class LinkPagerActivity extends FragmentActivity
        implements LinkFragment.Callbacks, ListView.OnItemClickListener {
    ViewPager mViewPager;
    NavigationDrawerHelper mNavigationDrawerHelper;

    public void onLinkUpdated(NixMashupLink link) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        mNavigationDrawerHelper.handleOptionsItemSelected(item);
        return handled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mViewPager = new ViewPager(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setId(R.id.viewPager);
//        setContentView(mViewPager);
//        setContentView(R.layout.activity_fragment);

        final ArrayList<NixMashupLink> links = Links.get(this).getLinks();

        FragmentManager fm = getSupportFragmentManager();
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

        mNavigationDrawerHelper = new NavigationDrawerHelper();
        mNavigationDrawerHelper.init(this, this);

        Intent startupIntent = getIntent();
        int tagPosition =
                startupIntent.getIntExtra(
                        SingleFragmentActivity.EXTRA_TAG,
                        SingleFragmentActivity.EXTRA_TAG_NOT_SET);

        if (tagPosition != SingleFragmentActivity.EXTRA_TAG_NOT_SET) {
            mNavigationDrawerHelper.setSelection(tagPosition);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinkUtils.prepareCategorySearch(this,
                getResources().getStringArray(R.array.category_search_array)[position]);
        LinkListActivity.tagPosition = position;
        Intent intent = new Intent(this, LinkListActivity.class);
        intent.putExtra(SingleFragmentActivity.EXTRA_TAG, position);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mNavigationDrawerHelper.syncState();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mNavigationDrawerHelper.handleOnPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mNavigationDrawerHelper.syncState();
        super.onConfigurationChanged(newConfig);
    }
}

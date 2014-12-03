package com.nixmash.android.links;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import static android.widget.AdapterView.OnItemClickListener;


public abstract class SingleFragmentActivity extends FragmentActivity
        implements OnItemClickListener {
    protected abstract Fragment createFragment();

    public static final String EXTRA_TAG = "nixmashuplinks.EXTRA_TAG";
    public static final int EXTRA_TAG_NOT_SET = -1;
    public static final String PHONE_SCREEN_TYPE = "phone";

    NavigationDrawerHelper mNavigationDrawerHelper;

    @Override
    public void onNewIntent(Intent intent) {
        int tagPosition = intent.getIntExtra(EXTRA_TAG, EXTRA_TAG_NOT_SET);
        if (tagPosition != EXTRA_TAG_NOT_SET)
        {
            mNavigationDrawerHelper.setSelection(tagPosition);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

        mNavigationDrawerHelper = new NavigationDrawerHelper();
        mNavigationDrawerHelper.init(this, this);

        Intent startupIntent = getIntent();
        int tagPosition = startupIntent.getIntExtra(EXTRA_TAG, EXTRA_TAG_NOT_SET);
        if (tagPosition != EXTRA_TAG_NOT_SET)
        {
            mNavigationDrawerHelper.setSelection(tagPosition);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinkListFragment fragment = (LinkListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
        LinkUtils.prepareCategorySearch(fragment.getActivity(),
                getResources().getStringArray(R.array.category_search_array)[position]);
        LinkListActivity.tagPosition = position;
        fragment.updateItems();
        mNavigationDrawerHelper.handleSelect(position);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        mNavigationDrawerHelper.handleOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mNavigationDrawerHelper.syncState();
        super.onConfigurationChanged(newConfig);
    }
}


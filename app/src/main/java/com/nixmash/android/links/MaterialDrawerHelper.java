package com.nixmash.android.links;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MaterialDrawerHelper implements CategoryAdapter.OnItemClickListener {


    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private LinearLayoutManager mLayoutManager;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mCategories;

    public void init(ActionBarActivity theActivity) {

        initView(theActivity);

        if (mToolbar != null)

        {
            mToolbar.setTitle(R.string.app_name);
            theActivity.setSupportActionBar(mToolbar);
        }

        initDrawer(theActivity);


    }

    private void initView(Activity activity) {

        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        mTitle = mDrawerTitle = activity.getTitle();
        mCategories = activity.getResources().getStringArray(R.array.category_display_array);
        mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayout);
        mDrawerList = (RecyclerView) activity.findViewById(R.id.left_drawer);

        mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.scrollToPosition(0);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // improve performance by indicating the list if fixed size.
        mDrawerList.setHasFixedSize(true);
        mDrawerList.setLayoutManager(mLayoutManager);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(CategoryAdapter.newInstance(mCategories, this, BaseActivity.categoryPosition));

    }

    private void initDrawer(Activity activity) {

        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                ActionBarActivity actionBarActivity = (ActionBarActivity) view.getContext();
                actionBarActivity.getSupportActionBar().setTitle(mTitle);
                actionBarActivity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                ActionBarActivity actionBarActivity = (ActionBarActivity) drawerView.getContext();
                actionBarActivity.getSupportActionBar().setTitle(mDrawerTitle);
                actionBarActivity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }

    public void handleOnPrepareOptionsMenu(Menu menu) {
//        If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
    }

    public void handleOptionsItemSelected(MenuItem item) {
        mDrawerToggle.onOptionsItemSelected(item);
    }

    public void handleOnConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view, int position) {
        BaseActivity.categoryPosition = position;
        mDrawerList.setAdapter(CategoryAdapter.newInstance(mCategories, this, position));
        Activity activity = (Activity) view.getContext();
        LinkUtils.prepareCategorySearch(activity,
                NixMashupLinksApp.getContext().getResources().getStringArray(R.array.category_search_array)[position]);
        selectItem(activity, position);
    }

    private void selectItem(Activity activity, int position) {
        BaseActivity.categoryPosition = position;
        mDrawerLayout.closeDrawer(mDrawerList);
        LinkUtils.prepareCategorySearch(activity,
                activity.getResources().getStringArray(R.array.category_search_array)[position]);
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(BaseActivity.EXTRA_CATEGORY_POSITION, position);
        activity.startActivity(intent);
    }

}

package com.nixmash.android.links;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavigationDrawerHelper  {

    DrawerLayout mDrawerLayout;
    ListView mDrawerListView;
    ActionBarDrawerToggle mDrawerToggle;

    public void init(Activity theActivity, ListView.OnItemClickListener listener)  {
        mDrawerLayout = (DrawerLayout)  theActivity.findViewById(R.id.drawer_layout);
        mDrawerListView  = (ListView) theActivity.findViewById(R.id.left_drawer);


        String[] options = theActivity.getResources().getStringArray(R.array.category_display_array);
        ArrayAdapter<String> navigationDrawerAdapter =
                new ArrayAdapter<String>(theActivity, R.layout.drawer_list_item, options);
        mDrawerListView.setAdapter(navigationDrawerAdapter);
        mDrawerListView.setOnItemClickListener(listener);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerListView.setItemChecked(0, true);
        setupActionBar(theActivity);

    }

    private void setupActionBar(Activity theActivity) {
        final Activity activity = theActivity;
        ActionBar actionBar = theActivity.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(
                theActivity,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
        };
    }


    public void handleSelect(int option)  {
        mDrawerListView.setItemChecked(option, true);
        mDrawerLayout.closeDrawer(mDrawerListView);

    }

    public void handleOnPrepareOptionsMenu(Menu menu)  {
        boolean itemVisible = ! mDrawerLayout.isDrawerOpen(mDrawerListView);
        for (int index = 0;  index < menu.size();  index++)  {
            MenuItem item = menu.getItem(index);
            item.setEnabled(itemVisible);
        }

    }

    public void handleOptionsItemSelected(MenuItem item)  {
        mDrawerToggle.onOptionsItemSelected(item);

    }

    public void syncState()  {
        mDrawerToggle.syncState();

    }

    public void setSelection(int option)  {
        mDrawerListView.setItemChecked(option, true);

    }

}

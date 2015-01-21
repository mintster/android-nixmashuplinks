package com.nixmash.android.links.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nixmash.android.links.util.CategoryAdapter;
import com.nixmash.android.links.util.MaterialDrawerHelper;
import com.nixmash.android.links.service.PollService;
import com.nixmash.android.links.R;

/**
 * Created by daveburke on 12/23/14.
 */
public abstract class BaseActivity extends ActionBarActivity
        implements CategoryAdapter.OnItemClickListener {

    // region properties

    public static final String EXTRA_CATEGORY_POSITION = "nixmashuplinks.EXTRA_CATEGORY_POSITION";
    public static final int EXTRA_CATEGORY_NOT_SET_ID = -1;
    public static final String PHONE_SCREEN_TYPE = "phone";

    public static int categoryPosition = 0;
    MaterialDrawerHelper mMaterialDrawerHelper;

    private static final int REQUEST_SEARCH = 0;

    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaterialDrawerHelper = new MaterialDrawerHelper();
        mMaterialDrawerHelper.init(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(this)) {
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
        mMaterialDrawerHelper.handleOnPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mMaterialDrawerHelper.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mMaterialDrawerHelper.handleOnConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMaterialDrawerHelper.handleOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position) {
        mMaterialDrawerHelper.onClick(view, position);
    }

}

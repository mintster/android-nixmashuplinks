package com.nixmash.android.links;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_about);
        super.onCreate(savedInstanceState);

        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = new AboutFragment();
            manager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem item= menu.findItem(R.menu.fragment_main_menu);
//        item.setVisible(false);
//        return true;
//    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuItem item = menu.findItem(R.menu.fragment_main_menu);
//        item.setVisible(false);
//        return true;
//    }
}

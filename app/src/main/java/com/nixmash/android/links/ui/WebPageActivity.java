package com.nixmash.android.links.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.nixmash.android.links.R;

public class WebPageActivity extends ActionBarActivity {

//    MaterialDrawerHelper mMaterialDrawerHelper;

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        this.setSupportActionBar(mToolbar);

//        if (NavUtils.getParentActivityName(this) != null)
//            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.webFragmentContainer);

        if (fragment == null) {
            fragment = new WebPageFragment();
            manager.beginTransaction()
                    .add(R.id.webFragmentContainer, fragment)
                    .commit();
        }

    }

}

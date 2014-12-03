package com.nixmash.android.links;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;

public class WebPageActivity extends FragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.webFragmentContainer);

        if (fragment == null) {
            fragment = new WebPageFragment();
            manager.beginTransaction()
                    .add(R.id.webFragmentContainer, fragment)
                    .commit();
        }
    }

}

package com.nixmash.android.links;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;


public class AboutActivity extends SingleFragmentActivity {

    @Override
    public android.support.v4.app.Fragment createFragment() {
        return new AboutFragment();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinkUtils.prepareCategorySearch(this,
                getResources().getStringArray(R.array.category_search_array)[position]);

        Intent intent = new Intent(this, LinkListActivity.class);
        intent.putExtra(EXTRA_TAG, position);
        startActivity(intent);
    }
}

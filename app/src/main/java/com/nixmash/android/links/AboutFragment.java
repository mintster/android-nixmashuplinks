package com.nixmash.android.links;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daveburke on 11/20/14.
 */
public class AboutFragment extends VisibleFragment {


    private static final int REQUEST_SEARCH = 0;
    private static final String DIALOG_SEARCH = "search";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_SEARCH) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                LinkUtils.resetCategoryFocus();
                FragmentManager fm = getActivity()
                        .getFragmentManager();
                SearchFragment dialog = SearchFragment
                        .newInstance();

                dialog.setTargetFragment(this, REQUEST_SEARCH);
                dialog.show(fm, DIALOG_SEARCH);
                return true;
            case R.id.menu_item_clear:
                LinkUtils.startMainActivity(getActivity());
                return true;
            case R.id.menu_item_refresh:
                LinkUtils.startMainActivity(getActivity());
                return true;
            case R.id.menu_item_about:
                LinkUtils.resetCategoryFocus();
                Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    getActivity().invalidateOptionsMenu();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

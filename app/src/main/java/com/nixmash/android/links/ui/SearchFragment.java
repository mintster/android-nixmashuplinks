package com.nixmash.android.links.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.nixmash.android.links.io.LinkFetchr;
import com.nixmash.android.links.R;

public class SearchFragment extends DialogFragment implements View.OnClickListener {

    String mSearch;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putString(LinkFetchr.PREF_TAG_QUERY, null)
                .putString(LinkFetchr.PREF_SEARCH_QUERY, mSearch)
                .commit();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, null);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_search, null);

        EditText mQueryField = (EditText) v.findViewById(R.id.dialog_queryText);
        mQueryField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mSearch = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.search_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }


    @Override
    public void onClick(View v) {

    }
}

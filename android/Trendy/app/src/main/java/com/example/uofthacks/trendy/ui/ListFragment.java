package com.example.uofthacks.trendy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uofthacks.trendy.R;
import com.example.uofthacks.trendy.adapters.InstagramListArrayAdapter;
import com.example.uofthacks.trendy.adapters.TwitterListArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 22/02/15.
 */
public class ListFragment extends Fragment {

    private ArrayAdapter<JSONObject> mAdapter;
    private android.support.v7.app.ActionBar.TabListener mTabListener;

    public static ListFragment newInstance(int sectionNumber, String json) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        args.putString("json", json);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = this.getArguments();
        JSONObject postItems;
        List<JSONObject> items = null;
        try {
            postItems = new JSONObject(bundle.getString("json"));
            int numItems = postItems.getInt("num");
            items = new ArrayList<JSONObject>();

            for (int i = 0; i < numItems; i++) {
                items.add(postItems.getJSONObject(String.valueOf(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        int sectionNumber = bundle.getInt("section_number");
        if (sectionNumber == 0) {
            if (items != null) {
                mAdapter = new TwitterListArrayAdapter(activity, items);
            } else {
                Log.e("PETER", "Problem creating twitter list adapter");
            }
        } else if (sectionNumber == 1) {
            if (items != null) {
                mAdapter = new InstagramListArrayAdapter(activity, items);
            } else {
                Log.e("PETER", "Problem creating instagram list adapter");
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int section = getArguments().getInt("section_number");
        View rootView = inflater.inflate(R.layout.list_layout, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        if (section == 0) {
            listView.setAdapter(mAdapter);
        } else if (section == 1) {
            listView.setAdapter(mAdapter);
        }
        return rootView;
    }
}

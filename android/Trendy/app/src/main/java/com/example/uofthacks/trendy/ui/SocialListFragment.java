package com.example.uofthacks.trendy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
public class SocialListFragment extends ListFragment {

    private ArrayAdapter<JSONObject> mAdapter;
    private int mSectionNumber;
    private List<JSONObject> mItems;

    public static SocialListFragment newInstance(int sectionNumber, String json) {
        SocialListFragment fragment = new SocialListFragment();
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
        mItems = items;

        mSectionNumber = bundle.getInt("section_number");
        if (mSectionNumber == 0) {
            if (items != null) {
                mAdapter = new TwitterListArrayAdapter(activity, items);
            } else {
                Log.e("PETER", "Problem creating twitter list adapter");
            }
        } else if (mSectionNumber == 1) {
            if (items != null) {
                mAdapter = new InstagramListArrayAdapter(activity, items);
            } else {
                Log.e("PETER", "Problem creating instagram list adapter");
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FrameLayout wrapper = new FrameLayout(getActivity());
        wrapper.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        wrapper.setId(100);

        final int section = getArguments().getInt("section_number");
        View rootView = inflater.inflate(R.layout.list_layout, container, false);
        ListView listView = (ListView) rootView.findViewById(android.R.id.list);

        listView.setAdapter(mAdapter);

        wrapper.addView(listView);
        return wrapper;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Bundle arguments = new Bundle();

        try {
            arguments.putString("text", mItems.get(position).getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arguments.putString("image_url", mItems.get(position).getString("image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arguments.putInt("section_number", mSectionNumber);

        getListView().setVisibility(View.GONE);

        ListDetailFragment newFragment = new ListDetailFragment(getListView());
        newFragment.setArguments(arguments);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null).add(100, newFragment);
//        transaction.add( newFragment, "list item detail");
        transaction.commit();

    }
}

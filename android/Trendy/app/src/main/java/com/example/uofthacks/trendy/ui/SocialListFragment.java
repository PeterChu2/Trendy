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
        final int section = getArguments().getInt("section_number");
        View rootView = inflater.inflate(R.layout.list_layout, container, false);
        ListView listView = getListView();
        if (section == 0) {
            listView.setAdapter(mAdapter);
            listView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(0x4099FF);
                    v.invalidate();
                }
            });
        } else if (section == 1) {
            listView.setAdapter(mAdapter);
            listView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(0x3F729B);
                    v.invalidate();
                }
            });
        }
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Bundle arguments = new Bundle();
//        if(mSectionNumber == 0) {
//            arguments.putString("text",(String) (((TextView) v.findViewById(R.id.tweet_text)).getText()));
            try
            {
                arguments.putString("text", mItems.get(position).getString("text"));
                arguments.putString("url", mItems.get(position).getString("image_url"));
                arguments.putInt("section_number", mSectionNumber);
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
//        }
//        else if(mSectionNumber == 1) {
//            arguments.putString("text", (String) (((TextView) v.findViewById(R.id.instagram_caption)).getText()));
//            arguments.putString("url", "A");
//        }
        ListDetailFragment newFragment = new ListDetailFragment();
        newFragment.setArguments(arguments);
        FragmentTransaction transaction = (getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(this.getId(), newFragment);
        transaction.commit();
    }
}

package com.example.uofthacks.trendy.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.uofthacks.trendy.adapters.InstagramListArrayAdapter;
import com.example.uofthacks.trendy.R;
import com.example.uofthacks.trendy.adapters.TwitterListArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 2015-02-01.
 */
public class DetailFragment extends Fragment {

    private RelativeLayout mRelativeLayout;
    
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.footer);
        mRelativeLayout.setVisibility(View.GONE);
        List<JSONObject> instagramItems = new ArrayList<JSONObject>();
        List<JSONObject> twitterItems = new ArrayList<JSONObject>();

        Bundle bundle = this.getArguments();
        try {

            JSONObject data = new JSONObject(bundle.getString("data"));
            JSONObject instagramData = data.getJSONObject("instagram");
            JSONObject twitterData = data.getJSONObject("tweet");

            int numInstagramElements = instagramData.getInt("num");
            int numTwitterElements = twitterData.getInt("num");

            for (int i = 0; i < numInstagramElements; i++) {
                instagramItems.add(instagramData.getJSONObject(String.valueOf(i)));
            }
            for (int i = 0; i < numTwitterElements; i++) {
                twitterItems.add(twitterData.getJSONObject(String.valueOf(i)));
            }

        } catch (JSONException e) {
            Log.e("PETER", "JSON FORMAT IS NOT RIGHT" + e.getMessage());
        }

        InstagramListArrayAdapter instagramAdapter = new InstagramListArrayAdapter(getActivity(),
                instagramItems);
        TwitterListArrayAdapter twitterAdapter = new TwitterListArrayAdapter(getActivity(),
                twitterItems);

        View view = inflater.inflate(R.layout.view_fragment_layout, container, false);
        ListView twitterListView = (ListView) view.findViewById(R.id.twitter_list_view);
        ListView instagramListView = (ListView) view.findViewById(R.id.instagram_list_view);
        instagramListView.setAdapter(instagramAdapter);
        twitterListView.setAdapter(twitterAdapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRelativeLayout.setVisibility(View.VISIBLE);
    }

}

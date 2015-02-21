package com.example.uofthacks.trendy;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Upsight on 2015-02-01.
 */
public class DetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        List<String> instagramItems = new ArrayList<String>();
        List<String> twitterItems = new ArrayList<String>();

        Bundle bundle = this.getArguments();
        try {

            JSONObject data = new JSONObject(bundle.getString("data"));
            JSONObject instagramData = data.getJSONObject("instagram");
            JSONObject twitterData = data.getJSONObject("tweet");

            int numInstagramElements = instagramData.getInt("num");
            int numTwitterElements = twitterData.getInt("num");

            for (int i = 0; i < numInstagramElements; i++) {
                instagramItems.add(instagramData.getJSONObject(String.valueOf(i)).getString("text"));
            }
            for (int i = 0; i < numTwitterElements; i++) {
                twitterItems.add(twitterData.getJSONObject(String.valueOf(i)).getString("text"));
            }


        } catch (JSONException e) {
            Log.e("PETER", "JSON FORMAT IS NOT RIGHT" + e.getMessage());
        }

        ArrayAdapter<String> instagramAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.instagram_item, instagramItems);
        ArrayAdapter<String> twitterAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.twitter_item, twitterItems);

        View view = inflater.inflate(R.layout.view_fragment_layout, container, false);
        ListView twitterListView = (ListView) view.findViewById(R.id.twitter_list_view);
        ListView instagramListView = (ListView) view.findViewById(R.id.instagram_list_view);
        instagramListView.setAdapter(instagramAdapter);
        twitterListView.setAdapter(twitterAdapter);


        return view;
    }


}
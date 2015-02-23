package com.example.uofthacks.trendy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.uofthacks.trendy.R;
import com.example.uofthacks.trendy.adapters.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 2015-02-01.
 */
public class DetailFragment extends Fragment {

    private RelativeLayout mRelativeLayout;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.footer);
        mRelativeLayout.setVisibility(View.GONE);
        List<JSONObject> instagramItems = new ArrayList<JSONObject>();
        List<JSONObject> twitterItems = new ArrayList<JSONObject>();
        JSONObject instagramData = null;
        JSONObject twitterData = null;

        Bundle bundle = this.getArguments();
        try {

            JSONObject data = new JSONObject(bundle.getString("data"));
            instagramData = data.getJSONObject("instagram");
            twitterData = data.getJSONObject("tweet");

        } catch (JSONException e) {
            Log.e("PETER", "JSON FORMAT IS NOT RIGHT" + e.getMessage());
        }


        View view = inflater.inflate(R.layout.view_fragment_layout, container, false);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(),
                getActivity(), twitterData.toString(), instagramData.toString());
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRelativeLayout.setVisibility(View.VISIBLE);
    }

}

package com.example.uofthacks.trendy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uofthacks.trendy.R;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by peter on 23/02/15.
 */
public class ListDetailFragment extends Fragment {

    private View mView;

    public ListDetailFragment(View view) {
        mView = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_detail_layout, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        TextView textView = (TextView) rootView.findViewById(R.id.text);
        String text = (String) getArguments().get("text");
        int sectionNumber = getArguments().getInt("section_number");

        try {
            if ((getArguments().get("image_url")) != null) {
                String imageURL = (new JSONArray((String) getArguments().get("image_url"))).getString(0);
                if (sectionNumber == 0) {
                    Ion.with(imageView)
                            .placeholder(R.drawable.twitter_icon)
                            .load(imageURL);
                } else {
                    Ion.with(imageView)
                            .placeholder(R.drawable.instagram_icon)
                            .load(imageURL);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (text != null) {
            textView.setText(text);
        }

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mView.setVisibility(View.VISIBLE);
    }
}

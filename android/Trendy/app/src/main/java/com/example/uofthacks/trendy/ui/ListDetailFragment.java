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
        View rootView;

        String text = (String) getArguments().get("text");
        String urlString = (String) getArguments().get("image_url");
        int sectionNumber = getArguments().getInt("section_number");
        String imageURL = null;
        try {
            imageURL = (new JSONArray(urlString)).getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // determine type of layout base on info available
        if ((text == null) || (text.isEmpty())) {
            rootView = inflater.inflate(R.layout.list_detail_picture_only, container, false);
        } else if ((imageURL == null) || (imageURL.isEmpty())) {
            rootView = inflater.inflate(R.layout.list_detail_text_only, container, false);
        } else {
            rootView = inflater.inflate(R.layout.list_detail_layout, container, false);
        }

        if ((imageURL != null) && (!imageURL.isEmpty())) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image);

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


        if ((text != null) && (!text.isEmpty())) {
            TextView textView = (TextView) rootView.findViewById(R.id.text);
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

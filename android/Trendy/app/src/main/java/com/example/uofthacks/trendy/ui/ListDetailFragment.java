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

/**
 * Created by peter on 23/02/15.
 */
public class ListDetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_detail_layout, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        TextView textView = (TextView) rootView.findViewById(R.id.text);
        textView.setText( (String) getArguments().get("text"));
        int sectionNumber = getArguments().getInt("section_number");
        if (sectionNumber == 0) {
            Ion.with(imageView)
                    .placeholder(R.drawable.twitter_icon)
                    .load((String) getArguments().get("url"));
            return rootView;
        }
        else {
            Ion.with(imageView)
                    .placeholder(R.drawable.instagram_icon)
                    .load((String) getArguments().get("url"));
            return rootView;
        }
    }

}

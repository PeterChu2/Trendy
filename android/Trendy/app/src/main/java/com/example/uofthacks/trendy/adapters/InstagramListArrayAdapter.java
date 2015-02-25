package com.example.uofthacks.trendy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uofthacks.trendy.R;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by peter on 19/02/15.
 */
public class InstagramListArrayAdapter extends ArrayAdapter<JSONObject> {

    private Context context;

    public InstagramListArrayAdapter(Context context, List<JSONObject> data) {
        super(context, R.layout.instagram_item, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        JSONObject data = getItem(position);

        if (data != null) {
            String text = null;
            String imageURL = null;
            // get data
            try {
                text = data.getString("text");
                imageURL = data.getJSONArray("image_url").getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            // inflate a different layout depending if a caption is available
            if ((text != null) && !text.isEmpty()) {
                row = inflater.inflate(R.layout.instagram_item, parent, false);
                TextView instagramCaption = (TextView) row.findViewById(R.id.instagram_caption);
                instagramCaption.setText(text);
            } else {
                row = inflater.inflate(R.layout.instagram_item_no_caption, parent, false);
            }

            // Use Ion Library to load image because it supports caching and
            // listview adapter support
            ImageView instagramPicture = (ImageView) row.findViewById(R.id.instagram_picture);
            Ion.with(instagramPicture)
                    .placeholder(R.drawable.twitter_icon)
                    .load(imageURL);

        }
        return row;
    }

}

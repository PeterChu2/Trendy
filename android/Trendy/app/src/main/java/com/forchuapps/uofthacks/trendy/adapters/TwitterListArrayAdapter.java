package com.forchuapps.uofthacks.trendy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.forchuapps.uofthacks.trendy.R;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by peter on 21/02/15.
 */
public class TwitterListArrayAdapter extends ArrayAdapter<JSONObject> {

    private Context context;

    public TwitterListArrayAdapter(Context context, List<JSONObject> data) {
        super(context, R.layout.twitter_item, data);
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

            // inflate a different layout depending if a picture is available
            if (imageURL != null) {
                row = inflater.inflate(R.layout.twitter_item, parent, false);
                // Use Ion Library to load image because it supports caching and
                // listview adapter support
                ImageView tweetPicture = (ImageView) row.findViewById(R.id.tweet_picture);
                Ion.with(tweetPicture)
                        .placeholder(R.drawable.twitter_icon)
                        .load(imageURL);
            } else {
                row = inflater.inflate(R.layout.twitter_item_no_picture, parent, false);
            }

            TextView tweetText = (TextView) row.findViewById(R.id.tweet_text);
            if (text != null) {
                tweetText.setText(text);
            }

        }
        return row;

    }

}



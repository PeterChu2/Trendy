package com.example.uofthacks.trendy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by peter on 19/02/15.
 */
public class InstagramListArrayAdapter extends ArrayAdapter<JSONObject> {

    private Context context;

    public InstagramListArrayAdapter(Context context, List<JSONObject> data)
    {
        super(context, R.layout.instagram_item, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // check if view is null, if it is, must inflate it
//        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.instagram_item, parent, false);
//        }

        JSONObject data = getItem(position);
        if(data != null) {
            ImageView instagramPicture = (ImageView) row.findViewById(R.id.instagram_picture);
            TextView instagramCaption = (TextView) row.findViewById(R.id.instagram_caption);

            try {
                instagramCaption.setText(data.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                String imageURL = data.getJSONArray("image_url").getString(0);
                if(imageURL != null)
                {
                    // Use Ion Library to load image because it supports caching and
                    // listview adapter support
                    Ion.with(instagramPicture)
                            .placeholder(R.drawable.instagram_icon)
                            .load(imageURL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return row;
    }

}

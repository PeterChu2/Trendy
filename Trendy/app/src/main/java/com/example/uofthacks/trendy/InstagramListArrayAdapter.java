package com.example.uofthacks.trendy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by peter on 19/02/15.
 */
public class InstagramListArrayAdapter extends ArrayAdapter<String> {

    public InstagramListArrayAdapter(Context context, int layoutResourceId, String[] data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageView instagramPicture;
        TextView instagramCaption;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            instagramPicture = (ImageView)row.findViewById(R.id.instagram_picture);
            instagramCaption = (TextView)row.findViewById(R.id.instagram_caption);

        }
        else
        {
        }

        return row;
    }

}

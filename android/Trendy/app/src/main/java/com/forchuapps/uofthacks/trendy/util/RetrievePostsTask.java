package com.forchuapps.uofthacks.trendy.util;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.forchuapps.uofthacks.trendy.R;
import com.forchuapps.uofthacks.trendy.ui.DetailFragment;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 21/02/15.
 */
public class RetrievePostsTask extends AsyncTask<LatLng, Void, String> {
    private FragmentActivity mActivity;

    public RetrievePostsTask(FragmentActivity activity) {
        mActivity = activity;
    }

    protected String doInBackground(final LatLng... latLng) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://trendy-posts.herokuapp.com/nearby");
        HttpResponse response;
        //TO DO
        int distanceRadius = 100;
        // Add parameters required
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(latLng[0].latitude)));
        nameValuePairs.add(new BasicNameValuePair("long", String.valueOf(latLng[0].longitude)));
        nameValuePairs.add(new BasicNameValuePair("value", String.valueOf(distanceRadius)));
        String json = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);

            try {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return null;
                }
                json = EntityUtils.toString(entity);

            } catch (IOException e) {
                // NOOP
            }
        } catch (ClientProtocolException e) {
            // NOOP
        } catch (IOException e) {
            // NOOP
        }

        return json;


    }

    protected void onPostExecute(String json) {
        DetailFragment fr = new DetailFragment();
        FragmentManager fm = mActivity.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        if (json == null) {
            Log.e("ERROR", "ERROR with json - it is null");
        }
        bundle.putString("data", json);
        fr.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.map, fr, "DetailFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
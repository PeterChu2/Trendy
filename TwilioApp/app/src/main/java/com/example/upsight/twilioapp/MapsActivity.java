package com.example.upsight.twilioapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.internal.pu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            mMap.setOnMapClickListener(
                    new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if (marker != null) {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .snippet(
                                            "Lat:" + latLng.latitude + "Lng:"
                                                    + latLng.longitude)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .title("ME"));
                            new RetrieveEventsTask().execute(latLng);
                        }
                    }
            );
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng currentLatLng;
        mMap.setMyLocationEnabled(true);
        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location currLocation = locationManager.getLastKnownLocation(provider);
        if(currLocation == null)
        {
            currentLatLng = new LatLng(43.6568775, -79.32085);
        }
        else {
            currentLatLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 16);
        mMap.animateCamera( cameraUpdate );
    }

    class RetrieveEventsTask extends AsyncTask<LatLng, Void, HttpResponse> {

        private Exception exception;

        protected HttpResponse doInBackground(final LatLng... latLng) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost("http://10.0.2.2:5000/nearby");
            HttpPost httppost = new HttpPost("http://192.168.43.76:5000/nearby");
            HttpResponse response;
            //TO DO
                int distanceRadius = 100;
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(latLng[0].latitude)));
                nameValuePairs.add(new BasicNameValuePair("long", String.valueOf(latLng[0].longitude)));
                nameValuePairs.add(new BasicNameValuePair("distance", String.valueOf(distanceRadius)));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);
                return response;
            }
            catch(ClientProtocolException e) {
                // NOOP
            }
            catch( IOException e) {
                // NOOP
            }

            return null;


        }

        protected void onPostExecute(HttpResponse response) {
            String json = null;
            Log.d("PETER", "http response received");
            try {
                if( response == null )
                {
                    return;
                }

                json = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                // NOOP
            }


            DetailFragment fr = new DetailFragment();
            FragmentManager fm = getFragmentManager();

            Bundle bundle = new Bundle();
            if(json == null)
            {
//                throw new JSONException("ERROR with json - it is null");
            }
            bundle.putString("data", json);
            fr.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.map, fr);
            fragmentTransaction.commit();
        }
    }
}

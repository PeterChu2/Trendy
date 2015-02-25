package com.example.uofthacks.trendy.ui;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.uofthacks.trendy.R;
import com.example.uofthacks.trendy.util.RetrievePostsTask;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity mActivity;
    private SeekBar mDistanceSeekBar;
    private Button mSearchButton;
    private Circle mCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // build the api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        mDistanceSeekBar = (SeekBar) findViewById(R.id.distanceSeekBar);
        mDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // circle will be set with radius from 0-20 km
                if (mCircle != null) {
                    mCircle.setRadius(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // search will be sent on click of the button
        mSearchButton = (Button) findViewById(R.id.searchButton);
        final View.OnClickListener onSearchButtonClickedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker != null) {
                    LatLng latLng = marker.getPosition();
                    new RetrievePostsTask(mActivity).execute(latLng);
                }
            }
        };
        mSearchButton.setOnClickListener(onSearchButtonClickedListener);

        mActivity = this;
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.footer);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(this);
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        updateMapLocation(mLastLocation);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        mGoogleApiClient.connect();
    }

    /*
     * Sets up map when it is ready
     */
    @Override
    public void onMapReady(GoogleMap map) {

        // define circle to overlay the marker to show distance
        mMap = map;
        mMap.setOnMapClickListener(
                new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        setMarker(latLng);
                    }
                }
        );

        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onLocationChanged(Location location) {
        // this callback is invoked when location updates
        updateMapLocation(location);
    }

    private void updateMapLocation(Location mLastLocation) {
        if (mLastLocation != null) {
            LatLng currentLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 12);
            // null check
            if (mMap != null) {
                mMap.animateCamera(cameraUpdate);
            }
            setMarker(currentLatLng);
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    private void setMarker(LatLng latLng) {
        if (marker != null) {
            // only one marker and one circle may exist at one time
            marker.remove();
            mCircle.remove();
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        int distance;
        if (mDistanceSeekBar != null) {
            distance = mDistanceSeekBar.getProgress();
        } else {
            distance = 10;
        }
        CircleOptions circleOptions = new CircleOptions()
                .radius(1000)
                .center(latLng);
        mCircle = mMap.addCircle(circleOptions);
        mCircle.setFillColor(Color.TRANSPARENT);
        mCircle.setStrokeColor(0x10000000);

        // immediately show circle
        mCircle.setRadius(distance * 1000);
    }


    @Override
    public void onBackPressed() {
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag != null) {
                if (frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0) {
                        childFm.popBackStack();
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }

}

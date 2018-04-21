package com.hkminibus.minibusdriver;

import android.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.location.Location.distanceBetween;

public class Driving  extends AppCompatActivity{
    route_data cRoute;
    TextView nextStop;
    int stopCount = 0;
    boolean[] passed;
    Button fullBtn;
    boolean fulled = false;
    public static List<stop_data> mStopList = new ArrayList<>();
    public StopAdapter mStopAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    double currentLat = 0.0;
    double currentLng = 0.0;
    boolean mTrackingLocation;
    LocationCallback mLocationCallback;

    public MediaPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving);
        cRoute = getIntent().getParcelableExtra("cRoute");
        setTitle(cRoute.getmRouteName());
        sound = MediaPlayer.create(this, R.raw.minibus);

        //Display next stop
        nextStop = (TextView) findViewById(R.id.nextStop);
        nextStop.setText(cRoute.getmStopList().get(stopCount).getName());
        passed = new boolean[cRoute.getmStopList().size()];

        //read stop list from firebase
        Query stop_query = mRef.child("Stop/" + cRoute.getmRouteID());
        stop_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStopList.clear();
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    stop_data s = ds.getValue(stop_data.class);
                    //do not display previous stops
                    i += 1;
                    if (stopCount < i) {
                        mStopList.add(s);
                    }
                }
                mStopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //set recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.stop_list);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this, DividerDecoration.VERTICAL_LIST));
        mStopAdapter = new StopAdapter(this, mStopList);
        mRecyclerView.setAdapter(mStopAdapter);

        //set full button
        fullBtn = (Button) findViewById(R.id.fullBtn);
        //Click full btn and update the firebase
        fullBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //true = pressed
                if (fulled) {
                    mRef.child("Driving").child(Menu.wrote_Did).child("full").setValue(false);
                    fulled = false;
                    Toast.makeText(getBaseContext(), "已取消", Toast.LENGTH_SHORT).show();
                } else {
                    mRef.child("Driving").child(Menu.wrote_Did).child("full").setValue(true);
                    fulled = true;
                    Toast.makeText(getBaseContext(), "已按", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (mTrackingLocation) {
                    Location c = locationResult.getLastLocation();
                    currentLat = c.getLatitude();
                    currentLng = c.getLongitude();
                    Log.v("Location",currentLat+","+currentLng);
                    updateNextStop();
                    if(Menu.wrote_Did!=null){
                        mRef.child("Driving").child(Menu.wrote_Did).child("lat").setValue(currentLat);
                        mRef.child("Driving").child(Menu.wrote_Did).child("lng").setValue(currentLng);
                        mRef.child("Driving").child(Menu.wrote_Did).child("stopName").setValue(nextStop.getText());
                    }
                }
            }
        };
        startTrackingLocation();

        //end driving when arrived the last stop
        if (passed[cRoute.getmStopList().size()-1]){
            stopTrackingLocation();
            mRef.child("Driving").child(Menu.wrote_Did).child("driving").setValue(false);
            //pass to driving record
        }
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        stopTrackingLocation();
        mRef.child("Driving").child(Menu.wrote_Did).child("driving").setValue(false);
    }

    public void updateNextStop() {
        stop_data c = cRoute.getmStopList().get(stopCount);
        float[] dist = new float[1];
        distanceBetween(currentLat, currentLng, c.getLatitude(), c.getLongitude(), dist);
        if (dist[0] < 10) {
            passed[stopCount] = true;
        }
        if (dist[0] > 10 && passed[stopCount]) {
            stopCount += 1;
            nextStop.setText(cRoute.getmStopList().get(stopCount).getName());
            if(Menu.wrote_Did!=null){
                mRef.child("Driving").child(Menu.wrote_Did).child("nextStop").setValue(false);
            }
        }
        if(Menu.wrote_Did!=null){
            //set red if passenger want to get off at next stop
            Query offQuery = mRef.child("Driving/"+Menu.wrote_Did+"/nextStop");
            offQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean b = dataSnapshot.getValue(Boolean.class);
                    Log.v("nextStop", String.valueOf(b));
                    if(b){
                        nextStop.setTextColor(Color.rgb(255,0, 0));
                        sound.start();
                    }
                    else {nextStop.setTextColor(Color.rgb(00,0, 0));}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(), mLocationCallback,
                            null /* Looper */);
            }
    }

    private void stopTrackingLocation() {
            mTrackingLocation = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTrackingLocation();
                }
                break;
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
}
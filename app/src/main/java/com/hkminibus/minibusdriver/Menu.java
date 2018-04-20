package com.hkminibus.minibusdriver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity implements LocationListener {
    driver_data currentDriver;
    TextView driverName, driverID;
    AutoCompleteTextView plateNo, routeNo, routeName;
    Button driveBtn, recordBtn, infoBtn;
    ArrayAdapter plateAdapter, routeNoAdapter, routeNameAdapter;
    ArrayList plateNoList = new ArrayList();
    ArrayList routeNoList = new ArrayList();
    ArrayList routeNameList = new ArrayList();
    route_data cRoute;
    car_data cCar;


    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();
    int count;
    public double currentLat = 0.0;
    public double currentLng = 0.0;
    ArrayList<stop_data> mStopList = new ArrayList<>();
    String driving_id;
    public static String wrote_Did;

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        currentDriver = getIntent().getParcelableExtra("CDriver");

        driverName = (TextView) this.findViewById(R.id.driverName);
        driverName.setText(" 你好，" + currentDriver.getName() + "司機！");
        driverID = (TextView) this.findViewById(R.id.driverID);
        driverID.setText("司機編號：" + currentDriver.getId());

        for (car_data c : MainActivity.allCar) {
            if (!plateNoList.contains(c.getmPlateNo())) {
                plateNoList.add(c.getmPlateNo());
            }
        }
        plateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, plateNoList);
        plateNo = (AutoCompleteTextView) this.findViewById(R.id.plateNo);
        plateNo.setThreshold(1);
        plateNo.setAdapter(plateAdapter);
        plateNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        for (route_data r : MainActivity.allRouteData) {
            if (!routeNoList.contains(r.getmRouteNo())) {
                routeNoList.add(r.getmRouteNo());
            }
        }
        routeNoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, routeNoList);
        routeNo = (AutoCompleteTextView) this.findViewById(R.id.routeNo);
        routeNo.setThreshold(0);
        routeNo.setAdapter(routeNoAdapter);
        routeNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        for (route_data r : MainActivity.allRouteData) {
            if (!routeNameList.contains(r.getmRouteName())) {
                routeNameList.add(r.getmRouteName());
            }
        }
        routeNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, routeNameList);
        routeName = (AutoCompleteTextView) this.findViewById(R.id.routeName);
        routeName.setThreshold(0);
        routeName.setAdapter(routeNameAdapter);
        routeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        //Get lng and lat检查权限
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);

        Log.v("OnlocationChange",  String.valueOf(currentLat) + " " + String.valueOf(currentLng));


        driveBtn = (Button) this.findViewById(R.id.driveBtn);
        driveBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    //build data
                    String mRouteName = routeName.getText().toString();
                    String mRouteNo = routeNo.getText().toString();
                    String mPlateNo = plateNo.getText().toString().toUpperCase();
                    String carSize = cCar.getCarSize();
                    String type = cRoute.getType();
                    mStopList.addAll(cRoute.getmStopList());
                    String mRouteID = cRoute.getmRouteID();

                    writeFb(carSize,mPlateNo,mRouteName,mRouteNo,type,mRouteID,mStopList);

                    Intent i = new Intent(getBaseContext(), Driving.class);
                    i.putExtra("cRoute", cRoute);
                    i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);

                }
            }
        });
    }

    public boolean validateInput() {
        boolean valid = false;
        if (!plateNoList.contains(plateNo.getText().toString())) {
            plateNo.setError("車牌號碼錯誤");
        }
        if (!routeNoList.contains(routeNo.getText().toString())) {
            routeNo.setError("找不到此路線");
        }
        if (!routeNameList.contains(routeName.getText().toString())) {
            routeName.setError("找不到此路線");
        }
        for (route_data r : MainActivity.allRouteData) {
            if (r.getmRouteName().matches(routeName.getText().toString())){
                if(r.getmRouteNo().matches(routeNo.getText().toString())){
                    cRoute = r;
                    for (car_data c : MainActivity.allCar) {
                        if (c.getmPlateNo().matches(plateNo.getText().toString())){
                            if(c.getType().matches(r.getType())){
                                cCar = c;
                                valid = true;
                            } else {
                                Toast.makeText(getBaseContext(), "小巴類別與行車路線不乎", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "路線編號與行車路線不乎", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return valid;
    }

    public void writeFb(final String carSize, final String mPlateNo, final String mRouteName, final String mRouteNo,
                          final String type, final String mRouteID, final ArrayList<stop_data> mStopList) {

        final DatabaseReference drivingRef = mRef.child("Driving");

        //make stoplist
        Query stop_query = mRef.child("Stop").child(mRouteID);
        stop_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStopList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    stop_data s = ds.getValue(stop_data.class);
                    mStopList.add(s);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //count the id
        drivingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get the child number
                count = (int) dataSnapshot.getChildrenCount() + 1;
                driving_id = "D" + String.valueOf(count < 1000 ? "000" : "") + count;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //write data to drivingRef
        drivingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //將資料放入driving_db
                driving_db new_record = new driving_db(carSize, true, false,
                        currentLat, currentLng, mPlateNo, mRouteName, mRouteNo,
                        false, mStopList, type);

                //將new_record放人子目錄 /ID
                drivingRef.child(driving_id).setValue(new_record);
                wrote_Did = driving_id;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}
}

package com.hkminibus.minibusdriver;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {
    public static List<route_data> allRouteData = new ArrayList<>();
    public static List<stop_data> allStop = new ArrayList<>();
    public static List<driver_data> allDriver = new ArrayList<>();
    public static List<car_data> allCar = new ArrayList<>();
    public static driver_data currentUser;

    private Toolbar toolbar;
    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        allStop = getIntent().getBundleExtra("bundle").getParcelableArrayList("allStop");
        allRouteData = getIntent().getBundleExtra("bundle").getParcelableArrayList("allRouteData");
        allDriver = getIntent().getBundleExtra("bundle").getParcelableArrayList("allDriver");
        allCar = getIntent().getBundleExtra("bundle").getParcelableArrayList("allCar");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ToolbarHelper.addMiddleTitle(this, "Minibus+", toolbar);

        final EditText driverID = (EditText) findViewById(R.id.driverID);
        final EditText password = (EditText) findViewById(R.id.password);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }

        loginBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                boolean correctID=false;
                boolean correctPW=false;
                for (driver_data d:allDriver) {
                    if (d.getId().matches(driverID.getText().toString())) {
                        correctID = true;
                        if (d.getPassword().matches(password.getText().toString())){
                            correctPW = true;
                            currentUser = d;
                        }
                    }
                }

                if (!correctID){
                    Toast.makeText(getBaseContext(), "帳號錯誤",Toast.LENGTH_SHORT).show();
                }
                else if (!correctPW){
                    Toast.makeText(getBaseContext(), "密碼錯誤",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    //pass to next activity
                    Intent i = new Intent(getBaseContext(),Menu.class);
                    i.putExtra("CDriver",currentUser);
                    i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);}
                    else {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}

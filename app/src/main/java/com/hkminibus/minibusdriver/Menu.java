package com.hkminibus.minibusdriver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {
    driver_data currentDriver;
    TextView driverName,driverID;
    AutoCompleteTextView plateNo,routeNo,routeName;
    Button driveBtn,recordBtn,infoBtn;
    ArrayAdapter plateAdapter,routeNoAdapter,routeNameAdapter;
    ArrayList plateNoList = new ArrayList();
    ArrayList routeNoList = new ArrayList();
    ArrayList routeNameList = new ArrayList();
    route_data cRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        currentDriver = getIntent().getParcelableExtra("CDriver");

        driverName = (TextView) this.findViewById(R.id.driverName);
        driverName.setText(" 你好，"+currentDriver.getName()+"司機！");
        driverID = (TextView)this.findViewById(R.id.driverID);
        driverID.setText("司機編號："+currentDriver.getId());

        for (car_data c : MainActivity.allCar) {
            if(!plateNoList.contains(c.getmPlateNo())){
                plateNoList.add(c.getmPlateNo());
            }
        }
        plateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,plateNoList);
        plateNo = (AutoCompleteTextView) this.findViewById(R.id.plateNo);
        plateNo.setThreshold(1);
        plateNo.setAdapter(plateAdapter);
        plateNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

        for (route_data r : MainActivity.allRouteData) {
            if(!routeNoList.contains(r.getmRouteNo())){
                 routeNoList.add(r.getmRouteNo());
            }
        }
        routeNoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,routeNoList);
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
            if(!routeNameList.contains(r.getmRouteName())){
                routeNameList.add(r.getmRouteName());
            }
        }
        routeNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,routeNameList);
        routeName = (AutoCompleteTextView) this.findViewById(R.id.routeName);
        routeName.setThreshold(0);
        routeName.setAdapter(routeNameAdapter);
        routeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

        driveBtn = (Button) this.findViewById(R.id.driveBtn);
        driveBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()){
                    Intent i = new Intent(getBaseContext(),Driving.class);
                    i.putExtra("cRoute",cRoute);
                    i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                }
             }
        });
    }
    public boolean validateInput(){
        boolean valid = false;
            if(!plateNoList.contains(plateNo.getText().toString())){
                plateNo.setError("車牌號碼錯誤");
            }
            if (!routeNoList.contains(routeNo.getText().toString())){
                routeNo.setError("找不到此路線");
            }
            if (!routeNameList.contains(routeName.getText().toString())){
                routeName.setError("找不到此路線");
            }
            for (route_data r:MainActivity.allRouteData){
                if (r.getmRouteName().matches(routeName.getText().toString())){
                    if (r.getmRouteNo().matches(routeNo.getText().toString())){
                        cRoute = r;
                        valid=true;
                    }
                    else {
                        Toast.makeText(getBaseContext(), "路線編號與行車路線不乎",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return valid;
    }
}

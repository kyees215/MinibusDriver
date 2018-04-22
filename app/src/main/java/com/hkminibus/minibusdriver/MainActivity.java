package com.hkminibus.minibusdriver;

import android.content.Intent;
import android.os.Bundle;
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
                        //it's not good to getId(), but idk why i cant use validatePassword(password). TBC
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
                    //pass to next activity
                    Intent i = new Intent(getBaseContext(),Menu.class);
                    //Bundle bundle = new Bundle();
                    //bundle.putParcelable("CDriver", currentUser);// 序列化
                    //i.putExtras(bundle);// 发送数据
                    i.putExtra("CDriver",currentUser);
                    i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                }
            }
        });
    }
}

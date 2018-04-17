package com.hkminibus.minibusdriver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class Driving  extends AppCompatActivity {
    Button fullBtn;
    route_data cRoute;
    TextView nextStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving);
        cRoute = getIntent().getParcelableExtra("cRoute");
        nextStop = (TextView) findViewById(R.id.nextStop);
        fullBtn = (Button) findViewById(R.id.fullBtn);
    }
}

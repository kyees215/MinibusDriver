package com.hkminibus.minibusdriver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class Driving  extends AppCompatActivity {
    Button fullBtn;
    route_data cRoute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving);
        cRoute = getIntent().getParcelableExtra("cRoute");
        fullBtn = (Button) this.findViewById(R.id.fullBtn);
    }
}

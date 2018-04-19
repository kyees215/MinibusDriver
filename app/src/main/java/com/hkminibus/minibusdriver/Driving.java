package com.hkminibus.minibusdriver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Driving  extends AppCompatActivity {
    Button fullBtn;
    route_data cRoute;
    String driving_id;
    TextView nextStop;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();
    boolean fulled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving);
        cRoute = getIntent().getParcelableExtra("cRoute");
        //mRef.child("Driving").addChildEventListener()

        nextStop = (TextView) findViewById(R.id.nextStop);
        fullBtn = (Button) findViewById(R.id.fullBtn);

        fullBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //true = pressed
                if (fulled){
                    mRef.child("Driving").child(driving_id).child("full").setValue(false);
                    fulled = false;
                    Toast.makeText(getBaseContext(), "已取消", Toast.LENGTH_SHORT).show();
                }
                else{
                    mRef.child("Driving").child(driving_id).child("full").setValue(true);
                    fulled = true;
                    Toast.makeText(getBaseContext(), "已按", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

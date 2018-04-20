package com.hkminibus.minibusdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Driving  extends AppCompatActivity {
    Button fullBtn;
    route_data cRoute;
    TextView nextStop;

    public static List<stop_data> mStopList = new ArrayList<>();
    public StopAdapter mStopAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();
    boolean fulled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving);
        cRoute = getIntent().getParcelableExtra("cRoute");

        //set waiting number
        Query stop_query = mRef.child("Stop/"+cRoute.getmRouteID());
        stop_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStopList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    stop_data s = ds.getValue(stop_data.class);
                    mStopList.add(s);
                }
                mStopAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //set stop list
        mRecyclerView = (RecyclerView) findViewById(R.id.stop_list);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this,DividerDecoration.VERTICAL_LIST));
        mStopAdapter = new StopAdapter(this, mStopList);
        mRecyclerView.setAdapter(mStopAdapter);



        nextStop = (TextView) findViewById(R.id.nextStop);
        fullBtn = (Button) findViewById(R.id.fullBtn);

        //Click full btn and update the firebase
        fullBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //true = pressed
                if (fulled){
                    mRef.child("Driving").child(Menu.wrote_Did).child("full").setValue(false);
                    fulled = false;
                    Toast.makeText(getBaseContext(), "已取消", Toast.LENGTH_SHORT).show();
                }
                else{
                    mRef.child("Driving").child(Menu.wrote_Did).child("full").setValue(true);
                    fulled = true;
                    Toast.makeText(getBaseContext(), "已按", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

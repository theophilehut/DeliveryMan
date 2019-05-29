package com.example.thophile.deliveryman;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PastDeliveriesActivity extends AppCompatActivity {
    private DeliveryManData dm;
    ArrayList<DeliveryData> deliveries = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_deliveries);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_past_deliveries);
        Log.d("PASTDEL", "recycler view : " + String.valueOf(recyclerView == null));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PastDeliveriesActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen");
        String username = getSharedPreferences("pref", MODE_PRIVATE).getString("username", "");
        Query queryRef = db.orderByChild("username").equalTo(username);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dm = dataSnapshot.getChildren().iterator().next().getValue(DeliveryManData.class);
                deliveries = dm.getPastDeliveries();
                Log.d("PASTDEL", "Number of deliveries : " + deliveries.size());
                //Display the rows
                CustomAdapter customAdapter = new CustomAdapter(PastDeliveriesActivity.this, deliveries);
                recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //  call the constructor of CustomAdapter to send the reference and data to Adapter




    }


}

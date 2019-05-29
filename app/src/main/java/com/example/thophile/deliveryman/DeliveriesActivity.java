package com.example.thophile.deliveryman;

import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DeliveriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DeliveryFragment.OnFragmentInteractionListener,
        View.OnClickListener {


    TextView TVStatus;
    DeliveryFragment fragmentDelivery;
    View fragmentDeliveryView;
    Button buttonFinishDelivery;

    private DeliveryManData dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveries);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TVStatus = findViewById(R.id.TV_deliveryman_status);
        fragmentDelivery = (DeliveryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_delivery);
        fragmentDeliveryView = findViewById(R.id.fragment_delivery);
        buttonFinishDelivery = findViewById(R.id.button_finishDelivery);
        buttonFinishDelivery.setOnClickListener(this);

        //get the currentDelivery;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen");
        String username = getSharedPreferences("pref", MODE_PRIVATE).getString("username", "");
        Query queryRef = db.orderByChild("username").equalTo(username);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dm = dataSnapshot.getChildren().iterator().next().getValue(DeliveryManData.class);
                Log.d("DELACT", "snapshot got = " + dataSnapshot.toString());
                Log.d("DELACT", "status delivery got = " + dm.getCurrentDelivery().getStatus());
                Log.d("DELACT", "status deliveryman got = " + dm.getStatus());
                switch(dm.getCurrentDelivery().getStatus()){
                    case DeliveryData.STATUSPROPOSAL :
                        dm.setStatus(DeliveryManData.DM_STATUSPROPOSAL);
                        break;
                    case DeliveryData.STATUSACCEPTED:
                        dm.setStatus(DeliveryManData.DM_STATUSONCOURSE);
                        break;
                    default:
                        dm.setStatus(DeliveryManData.DM_STATUSWAITING);
                }
                updateStatus(dm.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void saveState(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen");
        DataManager.uploadData(db, dm.getUsername(), dm);
        Log.w("DELACT", "save performed");
        //TODO update the status of delivery for the other actors
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deliveries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // TEMPORARY TESTING MENU ITEM
        if (id == R.id.addpropmenu){
            DeliveryData delivery1 = new DeliveryData("The coffee hub \n Corso Duca degli Abruzzi ", "Politecnico \n Corso Duca degli Abruzzi", "10.30am", DeliveryData.STATUSPROPOSAL);
            dm.setCurrentDelivery(delivery1);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen");
            DataManager.uploadData(db, dm.getUsername(), dm);
        }

        if(id == R.id.goto_pastDeliveries){
            Intent pastDel = new Intent(DeliveriesActivity.this,PastDeliveriesActivity.class);
            startActivity(pastDel);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            Intent profile = new Intent(DeliveriesActivity.this,Profile.class);
            startActivity(profile);

        } else if (id == R.id.nav_deliveries) {

            Intent todayMenu = new Intent(DeliveriesActivity.this,DeliveriesActivity.class);
            startActivity(todayMenu);
        }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void receiveDeliveryProposal(DeliveryData deliveryData){
        if (dm.getStatus() == DeliveryManData.DM_STATUSWAITING){
            showNotification("Delivery Received", "You received a delivery request, check it on your app.");
            updateStatus(DeliveryManData.DM_STATUSPROPOSAL);

        }
        else{
            //TODO : fill this with an impossibility notification to server

        }
    }

    public void updateStatus(int status){
        Log.d("DELACT", "Updating status");
        this.dm.setStatus(status);
        if (status == DeliveryManData.DM_STATUSONCOURSE){
            this.TVStatus.setText(getText(R.string.status_sentence_accepted));
            fragmentDelivery.displayDelivery(dm.getCurrentDelivery());
            fragmentDeliveryView.setVisibility(View.VISIBLE);
            buttonFinishDelivery.setVisibility(View.VISIBLE);

        }else if (status == DeliveryManData.DM_STATUSPROPOSAL ) {
            this.TVStatus.setText(getText(R.string.status_sentence_proposal));
            fragmentDelivery.displayProposal(dm.getCurrentDelivery());
            fragmentDeliveryView.setVisibility(View.VISIBLE);
            buttonFinishDelivery.setVisibility(View.INVISIBLE);

        }else if (status == DeliveryManData.DM_STATUSWAITING){
            this.TVStatus.setText(getText(R.string.status_sentence_waiting));
            fragmentDeliveryView.setVisibility(View.INVISIBLE);
            buttonFinishDelivery.setVisibility(View.INVISIBLE);
        }

        saveState();

    }

    // DEAL WITH NOTIFICATIONS
    void showNotification(String title, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), DeliveriesActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void getFragmentInteraction(int output) {
        if (output == DeliveryFragment.ACCEPTED){
            dm.getCurrentDelivery().setStatus(DeliveryData.STATUSACCEPTED);
            updateStatus(DeliveryManData.DM_STATUSONCOURSE);
        }
        else if (output == DeliveryFragment.REFUSED){
            dm.getCurrentDelivery().setStatus(DeliveryData.STATUSINVALID);
            updateStatus(DeliveryManData.DM_STATUSWAITING);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_finishDelivery:
                dm.finishCurrentDelivery();
                updateStatus(DeliveryManData.DM_STATUSWAITING);

        }
    }
}

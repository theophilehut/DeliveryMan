package com.example.thophile.deliveryman;

import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

public class DeliveriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DeliveryFragment.OnFragmentInteractionListener {

    TextView TVStatus;
    DeliveryFragment fragmentDelivery;
    View fragmentDeliveryView;

    public static int STATUSWAITING = 0;
    public static int STATUSPROPOSAL = 1;
    public static int STATUSACCEPTED = 2;

    private int status;
    private DeliveryData currentDelivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveries);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TVStatus = findViewById(R.id.TV_deliveryman_status);
        fragmentDelivery = (DeliveryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_delivery);
        fragmentDeliveryView = findViewById(R.id.fragment_delivery);

        //set up data persistency
        currentDelivery = new DeliveryData(-1, "","","");
        if (getSharedPreferences("deliv", MODE_PRIVATE).contains("id")) {
            status = getSharedPreferences("deliv", MODE_PRIVATE).getInt("status", STATUSPROPOSAL);
            TVStatus.setText(Integer.toString(status));
            if (status != STATUSWAITING) {
                currentDelivery = new DeliveryData(getSharedPreferences("deliv", MODE_PRIVATE).getInt("id", 1),
                        getSharedPreferences("deliv", MODE_PRIVATE).getString("restaurantAdress", "default"),
                        getSharedPreferences("deliv", MODE_PRIVATE).getString("customerAdress", "default"),
                        getSharedPreferences("deliv", MODE_PRIVATE).getString("deliveryTime", "default"));
                if (status == STATUSPROPOSAL) {
                    fragmentDelivery.newDeliveryProposal(currentDelivery);
                } else if (status == STATUSACCEPTED) {
                    fragmentDelivery.acceptDelivery();
                }
            } else {
                fragmentDelivery.refuseDelivery();
            }
        }
        else{
            status = STATUSWAITING;
            fragmentDelivery.refuseDelivery();
        }

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
        Log.w("SAVE", "save performed");
        getSharedPreferences("deliv",MODE_PRIVATE).edit().putInt("status",this.status).commit();
        getSharedPreferences("deliv",MODE_PRIVATE).edit().putInt("id",currentDelivery.getDeliveryID()).commit();
        getSharedPreferences("deliv",MODE_PRIVATE).edit().putString("customerAdress",currentDelivery.getDeliveryAdress()).commit();
        getSharedPreferences("deliv",MODE_PRIVATE).edit().putString("restaurantAdress",currentDelivery.getRestaurantAdress()).commit();
        getSharedPreferences("deliv",MODE_PRIVATE).edit().putString("deliveryTime",currentDelivery.getPickupTime()).commit();
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
            DeliveryData delivery1 = new DeliveryData(0,"The coffee hub \n Corso Duca degli Abruzzi ", "Politecnico \n Corso Duca degli Abruzzi", "10.30am");
            receiveDeliveryProposal(delivery1);
        }
        if (id == R.id.action_settings) {
            return true;
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
            finish();

        } else if (id == R.id.nav_deliveries) {

            Intent todayMenu = new Intent(DeliveriesActivity.this,DeliveriesActivity.class);
            startActivity(todayMenu);
            finish();

        }


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void receiveDeliveryProposal(DeliveryData deliveryData){
        if (status == STATUSWAITING){
            currentDelivery = deliveryData;
            fragmentDelivery.newDeliveryProposal(deliveryData);
            showNotification("Delivery Accepted", "Your delivery as been accepted. Go to restaurant adress ");
        }
        else{
            //TODO : fill this with an impossibility notification to server
        }
    }
    public void updateStatus(int status){
        this.status = status;
        if (status == STATUSACCEPTED){
            this.TVStatus.setText(getText(R.string.status_sentence_accepted));
            fragmentDeliveryView.setVisibility(View.VISIBLE);
        }else if (status == STATUSPROPOSAL ) {
            this.TVStatus.setText(getText(R.string.status_sentence_proposal));
            fragmentDeliveryView.setVisibility(View.VISIBLE);
        }else if (status == STATUSWAITING){
            this.TVStatus.setText(getText(R.string.status_sentence_waiting));
            fragmentDeliveryView.setVisibility(View.INVISIBLE);
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

    //GETTERS AND SETTERS
    public int getStatus() {
        return status;
    }

    public DeliveryData getCurrentDelivery() {
        return currentDelivery;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCurrentDelivery(DeliveryData currentDelivery) {
        this.currentDelivery = new DeliveryData(currentDelivery);
    }
}

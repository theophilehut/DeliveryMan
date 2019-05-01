package com.example.thophile.deliveryman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ImageButton b;
    private ImageButton cam;
    private EditText et_name;
    private EditText et_email;
    private EditText et_desc;
    private EditText et_phone;
    private Bitmap image;
    private Bitmap bitmap;
    private ImageView im;
    private String FILENAME = "profile_picture.png";
    private MenuItem save;
    private MenuItem edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cam = findViewById(R.id.button2);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_desc = findViewById(R.id.et_description);
        et_phone = findViewById(R.id.et_phone);


        et_name.setEnabled(false);
        et_email.setEnabled(false);
        et_desc.setEnabled(false);
        et_phone.setEnabled(false);

        et_name.setTextColor(Color.BLACK);
        et_email.setTextColor(Color.BLACK);
        et_desc.setTextColor(Color.BLACK);
        et_phone.setTextColor(Color.BLACK);
        cam.setVisibility(View.VISIBLE);

        im = findViewById(R.id.imageView);

        if (getSharedPreferences("pref", MODE_PRIVATE).contains("name")) {
            et_name.setText(getSharedPreferences("pref", MODE_PRIVATE).getString("name", ""));
            et_email.setText(getSharedPreferences("pref", MODE_PRIVATE).getString("email", ""));
            et_desc.setText(getSharedPreferences("pref", MODE_PRIVATE).getString("desc", ""));
            et_phone.setText(getSharedPreferences("pref", MODE_PRIVATE).getString("phone", ""));

            //if there is a bundle, use the saved image resource (if one is there)
            //image=savedInstanceState.getParcelable("BitmapImage");
            try {
                File filePath = getFileStreamPath(FILENAME);
                FileInputStream fi = new FileInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(fi);
                // ois.readObject();
                fi.close();
                // ois.close();
                image = bitmap;
                im.setImageBitmap(image);

            } catch (Exception ex) {

                Log.d("ERROR", ex.getMessage());
            }


        } else if (savedInstanceState != null) {

            et_name.setText(savedInstanceState.getString("name"));
            et_email.setText(savedInstanceState.getString("email"));
            et_desc.setText(savedInstanceState.getString("desc"));
            et_phone.setText(savedInstanceState.getString("phone"));

            //if there is a bundle, use the saved image resource (if one is there)
            image = savedInstanceState.getParcelable("BitmapImage");
            bitmap = image;
            im.setImageBitmap(image);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        save =(MenuItem)menu.findItem(R.id.Item02);
        edit =(MenuItem)menu.findItem(R.id.item01);
        save.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item01:
                et_name.setEnabled(true);
                et_email.setEnabled(true);
                et_desc.setEnabled(true);
                et_phone.setEnabled(true);
                et_name.setText(getSharedPreferences("pref",MODE_PRIVATE).getString("name",""));
                et_email.setText(getSharedPreferences("pref",MODE_PRIVATE).getString("email",""));
                et_desc.setText(getSharedPreferences("pref",MODE_PRIVATE).getString("desc",""));
                et_phone.setText(getSharedPreferences("pref",MODE_PRIVATE).getString("phone",""));
                edit.setVisible(false);
                save.setVisible(true);
                cam.setClickable(true);
                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage(Profile.this);
                    }
                });
                break;
            case R.id.Item02:
                //this part is for the persistence , however the preference can only save primitive types so , only the string variables
                getSharedPreferences("pref",MODE_PRIVATE).edit().putString("name",et_name.getText().toString()).commit();
                getSharedPreferences("pref",MODE_PRIVATE).edit().putString("desc",et_desc.getText().toString()).commit();
                getSharedPreferences("pref",MODE_PRIVATE).edit().putString("phone",et_phone.getText().toString()).commit();
                getSharedPreferences("pref",MODE_PRIVATE).edit().putString("email",et_email.getText().toString()).commit();
                edit.setVisible(true);
                save.setVisible(false);
                et_name.setEnabled(false);
                et_email.setEnabled(false);
                et_desc.setEnabled(false);
                et_phone.setEnabled(false);
                cam.setClickable(false);
                //getSharedPreferences("pref",MODE_PRIVATE).edit().putParcelable("BitmapImage", bitmap);
                try {
                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

// Writing the bitmap to the output stream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();




                }catch (Exception exception){

                    Log.e("ERROR",exception.getLocalizedMessage());

                }

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            Intent profile = new Intent(Profile.this,Profile.class);
            startActivity(profile);
            finish();

        } else if (id == R.id.nav_deliveries) {

            Intent todayMenu = new Intent(Profile.this,DeliveriesActivity.class);
            startActivity(todayMenu);
            finish();

        } /*else if (id == R.id.nav_reservations) {

            Intent reservations = new Intent(MainActivity.this,Reservations.class);
            startActivity(reservations);
            finish();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==0 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            //Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //ImageView im=findViewById(R.id.imageView);
            im.setImageBitmap(bitmap);
        }
        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmapp = (Bitmap)data.getExtras().get("data");
            bitmap=bitmapp;
            //ImageView im=findViewById(R.id.imageView);
            im.setImageBitmap(bitmap);
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 1);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 0);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}

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
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ImageButton b;
    private ImageButton cam;
    private EditText et_name;
    private EditText et_email;
    private EditText et_desc;
    private EditText et_phone;
    private Bitmap bitmap;
    private ImageView im;
    private String FILENAME = "profile_picture.png";
    private MenuItem save;
    private MenuItem edit;
    private String username;

    private DeliveryManData deliveryManData;


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

        username = getSharedPreferences("pref", MODE_PRIVATE).getString("username", "");

        // update data from database
        updateDataFromDB(username);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void updateDataFromDB(String identifier) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen/");
        Query queryRef = db.orderByChild("username").equalTo(identifier);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryManData = dataSnapshot.getChildren().iterator().next().getValue(DeliveryManData.class);
                Log.d("PROFILE", "getting data from DB : " + deliveryManData.getName());

                et_name.setText(deliveryManData.getName());
                et_phone.setText(deliveryManData.getPhone());
                et_email.setText(deliveryManData.getEmail());
                et_desc.setText(deliveryManData.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Add the image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("profilePictures/" + username +"/profile_picture.png");
        Log.d("PROFILE", "image path : " + imageRef.getPath());
        loadImage(imageRef, im);

    }

    public void loadImage(StorageReference imageRef, final ImageView view){
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert bytes data into a Bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Set the Bitmap data to the ImageView
                view.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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
        save = menu.findItem(R.id.Item02);
        edit = menu.findItem(R.id.item01);
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
                edit.setVisible(true);
                save.setVisible(false);
                et_name.setEnabled(false);
                et_email.setEnabled(false);
                et_desc.setEnabled(false);
                et_phone.setEnabled(false);
                cam.setClickable(false);
                try {
                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    // Writing the bitmap to the output stream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                }catch (Exception exception){
                    Log.e("ERROR",exception.getLocalizedMessage());
                }

                //Update the database
                    //Update data fields
                deliveryManData.setName(et_name.getText().toString());
                deliveryManData.setPhone(et_phone.getText().toString());
                deliveryManData.setEmail(et_email.getText().toString());
                deliveryManData.setDescription(et_desc.getText().toString());
                    //Get to the reference and update
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen/");
                Query queryRef = db.orderByChild("username").equalTo(username);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getChildren().iterator().next().getRef().setValue(deliveryManData);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                    //Add the picture
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                if (bitmap != null){
                    Uri uri = getImageUri(this, bitmap);
                    StorageReference storageReference = mStorageRef.child("profilePictures/" + username + "/" + FILENAME);
                    storageReference.putFile(uri);
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

        }
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
                e.printStackTrace();
            } catch (IOException e) {
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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Pic", null);
        return Uri.parse(path);
    }
}

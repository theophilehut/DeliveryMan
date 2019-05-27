package com.example.thophile.deliveryman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MenuItem confirm;
    private ImageButton cam;
    private Bitmap bitmap;
    private String FILENAME = "profile_picture.png";
    private EditText et_name;
    private EditText et_email;
    private EditText et_desc;
    private EditText et_phone;
    private ImageView im;

    //TODO cancel registration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cam = findViewById(R.id.button2);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_desc = findViewById(R.id.et_description);
        et_phone = findViewById(R.id.et_phone);
        im = findViewById(R.id.imageView);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(RegisterActivity.this);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        confirm =(MenuItem)menu.findItem(R.id.menuitem_confirm_registration);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Only one callable item

        if (et_name == null || et_name.getText().toString()==""
                || et_phone == null || et_phone.getText().toString()==""
                || et_email == null || et_email.getText().toString()==""){
            //If the profile has not the basic data, refuse it.
            Log.d("REG", "Data is not well filled to register");
            Log.d("REG", et_name.getText().toString());
            Toast.makeText(getApplicationContext(), "Enter at least your name, phone and e-mail.", Toast.LENGTH_SHORT).show();
        }
        else{
            //If the profile is validated
            // Register the data locally and on server
            getSharedPreferences("pref",MODE_PRIVATE).edit().putString("name",et_name.getText().toString()).commit();
            getSharedPreferences("pref",MODE_PRIVATE).edit().putString("phone",et_phone.getText().toString()).commit();
            getSharedPreferences("pref",MODE_PRIVATE).edit().putString("email",et_email.getText().toString()).commit();

            if (et_desc != null){
                getSharedPreferences("pref",MODE_PRIVATE).edit().putString("desc",et_desc.getText().toString()).commit();
            }

            if(bitmap != null) {
                try {
                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception exception) {
                    Log.e("ERROR", exception.getLocalizedMessage());
                }
            }
            //TODO register the data on DB
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen");
            DatabaseReference refAdd = db.push();
            HashMap<String, Object> newData = new HashMap<String, Object>();
            newData.put("username", getSharedPreferences("pref",MODE_PRIVATE).getString("username", ""));
            newData.put("password", getSharedPreferences("pref", MODE_PRIVATE).getString("password", ""));
            newData.put("name", et_name.getText().toString());
            newData.put("phone", et_phone.getText().toString());
            newData.put("email", et_email.getText().toString());
            newData.put("description", et_desc.getText().toString());
            //Finally update the database
            refAdd.updateChildren(newData);
            //Add the picture
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            if (bitmap != null){
                Uri uri = getImageUri(this, bitmap);
                StorageReference storageReference = mStorageRef.child("profilePictures/" + getSharedPreferences("pref",MODE_PRIVATE).getString("username", "") + "/" + FILENAME);
                storageReference.putFile(uri);
            }

                    //Finally update the database
            refAdd.updateChildren(newData);

            //Launch the normal use
            Intent profile = new Intent(RegisterActivity.this, Profile.class);
            Toast.makeText(getApplicationContext(), "Registration Confirmed!", Toast.LENGTH_SHORT).show();
            startActivity(profile);
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Pic", null);
        return Uri.parse(path);
    }

}

package com.example.thophile.deliveryman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
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

public class DataManager {

    public DeliveryManData dm;

    public DataManager() {
        dm = new DeliveryManData();
    }

    public static void loadImage(StorageReference imageRef, final ImageView view){
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

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Pic", null);
        return Uri.parse(path);
    }

    public static void uploadData(DatabaseReference ref, final String username,final Object obj){
        Query queryRef = ref.orderByChild("username").equalTo(username);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DATAMANAGER", "snapshot to upload : " + dataSnapshot.toString() + " for username : " + username);

                dataSnapshot.getChildren().iterator().next().getRef().setValue(obj);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public DeliveryManData downloadDataFromDB(DatabaseReference ref, String identifier) {
        Query queryRef = ref.orderByChild("username").equalTo(identifier);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DATA", "enter listener ");
                dm = dataSnapshot.getChildren().iterator().next().getValue(DeliveryManData.class);
                Log.d("DATA", "snapshot got = " + dataSnapshot.toString());
                Log.d("DATA", "status got = " + dm.getCurrentDelivery().getStatus());
                this.notify();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("DATA", "status got bis= " + dm.getCurrentDelivery().getStatus());
        return dm;
    }
}

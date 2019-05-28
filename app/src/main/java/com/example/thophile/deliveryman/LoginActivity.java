package com.example.thophile.deliveryman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener{

    EditText etUsername;
    EditText etPassword;
    Button buttonRegister;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.edit_username);
        etPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_register);
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        Log.d("LOGIN ", "Login created");
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_login:
                Log.d("LOGIN", "Login clicked");
                if (etPassword.getText() == null || etUsername == null) {
                    Toast.makeText(getApplicationContext(), "Username or password invalid", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen");
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                    Log.d("LOGIN", "Entering DB, " + username + ", " + password);
                    Query queryRef = db.orderByChild("username").equalTo(username);
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            Log.d("LOGIN", "OnDataChange entered, snapshot : " + snapshot.toString());
                            if(!snapshot.exists()){
                                Log.d("LOGIN", "No snapshot");
                                Toast.makeText(getApplicationContext(), "Username or password invalid", Toast.LENGTH_SHORT).show();
                            }
                            else if (snapshot.getChildren().iterator().next().child("password").getValue().equals(password)) {
                                Intent profile = new Intent(LoginActivity.this, Profile.class);
                                Toast.makeText(getApplicationContext(), "Registration Confirmed!", Toast.LENGTH_SHORT).show();
                                getSharedPreferences("pref", MODE_PRIVATE).edit().putString("username", username).commit();
                                startActivity(profile);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Username or password invalid", Toast.LENGTH_SHORT).show();
                                Log.d("LOGIN", "password expected : " + snapshot.getChildren().iterator().next().child("password").getValue());
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " +
                                    databaseError.getMessage());

                        }
                    });
                }


                break;
            case R.id.button_register:
                if (etPassword.getText() == null || etUsername == null) {
                    Toast.makeText(getApplicationContext(), "Username or password invalid", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("DeliveryMen");
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                    Log.d("LOGIN", "Entering DB, " + username + ", " + password);
                    Query queryRef = db.orderByChild("username").equalTo(username);
                    Log.d("LOGIN", "Query performed, " + username + ", " + password);
                    queryRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            Log.d("LOGIN", "OnDataChange entered, snapshot : " + snapshot.toString());
                            if(snapshot.exists()){
                                Log.d("LOGIN", "Existing snapshot");
                                Toast.makeText(getApplicationContext(), "Sorry, this username already exists", Toast.LENGTH_SHORT).show();
                            }
                            else if (username.length()<3 ) {
                                Toast.makeText(getApplicationContext(), "Username is too short.", Toast.LENGTH_SHORT).show();
                            }
                            else if (password.length()<6 ) {
                                Toast.makeText(getApplicationContext(), "Password is too short.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // Register launched
                                getSharedPreferences("pref",MODE_PRIVATE).edit().putString("username", username).commit();
                                getSharedPreferences("pref",MODE_PRIVATE).edit().putString("password", password).commit();
                                Intent registration = new Intent(LoginActivity.this, RegisterActivity.class);
                                startActivity(registration);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " +
                                    databaseError.getMessage());

                        }
                    });
                }


                Log.d("LOGIN", "Click register");
                break;

        }
    }
}

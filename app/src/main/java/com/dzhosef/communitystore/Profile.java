package com.dzhosef.communitystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class Profile extends AppCompatActivity {
    Button save_button;
    EditText username, phone, address;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseStorage.getInstance();
        assert user != null;
        String task=user.getUid();


        save_button =findViewById(R.id.button3);
        username =findViewById(R.id.editTextName);
        phone =findViewById(R.id.editText_phone);
        address =findViewById(R.id.editTextAddress);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child("ids").child(task);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() ) {

                    username.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    phone.setText(Objects.requireNonNull(snapshot.child("number").getValue()).toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        save_button.setOnClickListener(view -> {
            //String username= this.username.getText().toString();
            if (username.getText().toString().equals("")) {

                username.setError("enter name");
                username.requestFocus();
                return;
            }
            if (username.getText().toString().length()>20) {

                username.setError("user name too long");
                username.requestFocus();
                return;
            }
            if (phone.getText().toString().equals("")) {

                phone.setError("enter number");
                phone.requestFocus();
                return;
            }
            if (phone.getText().toString().length()>20) {

                phone.setError("number error");
                phone.requestFocus();
                return;
            }
            String location= address.getText().toString();
            if (address.getText().toString().equals("")) {

                address.setError("enter address");
                address.requestFocus();
                return;
            }
            if (address.getText().toString().length()>30) {

                address.setError("address too long");
                address.requestFocus();
                return;
            }


            long number=Long.parseLong(String.valueOf(phone.getText()));
            HelperClass2 helperClass = new HelperClass2(username.getText().toString(), number,location);
            try {
                myRef.setValue(helperClass);
            }catch (Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this,user.getDisplayName(),Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,MainActivity.class) );
            finish();
        }
    }
}
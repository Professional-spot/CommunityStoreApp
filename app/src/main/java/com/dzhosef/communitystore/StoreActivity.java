package com.dzhosef.communitystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {



    Button add;
    RecyclerView recyclerView;
    DatabaseReference database;
    My_adapter_2 my_adapter_2;
    ArrayList<Product2> list;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.userList22);
        database= FirebaseDatabase.getInstance().getReference("products");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        my_adapter_2 =new My_adapter_2(this ,list);
        recyclerView.setAdapter(my_adapter_2);
        user= mAuth.getCurrentUser();
        add=findViewById(R.id.button5);
        add.setOnClickListener(view -> {
            startActivity(new Intent(StoreActivity.this, Sell.class));
            finish();
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product2 product = dataSnapshot.getValue(Product2.class);
                        list.add(product);
                    }
                    my_adapter_2.filter(user.getUid());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void delete(String id,String uid){
        DatabaseReference myRef23;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        try {

        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child("product/"+uid+"/"+id+".jpg");

            desertRef.delete().addOnSuccessListener(aVoid -> {

            }).addOnFailureListener(exception -> {

            });
        }catch (Exception ignored){

        }

        myRef23 = FirebaseDatabase.getInstance().getReference().child("products").child(id);
        myRef23.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,MainActivity.class) );
            finish();
        }
    }


    }
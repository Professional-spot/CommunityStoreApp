package com.dzhosef.communitystore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "";
    private DrawerLayout mDrawerLayout;
    String username,number;
    TextView textView5,seller_name;
    boolean go;
    RecyclerView recyclerView;
    DatabaseReference database;
    private FirebaseAuth mAuth;
    My_adapter my_adapter;
    ArrayList<Product> list;
    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        go=true;
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView mDrawer = findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        View hView = mDrawer.getHeaderView(0);
        ImageView home_btn = findViewById(R.id.imageView_user_pic_profile_page);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        EditText editText=findViewById(R.id.search_id);
        TextView textview_none_2=findViewById(R.id.textView_none);
        home_btn.setOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.LEFT));
        textView5 = hView.findViewById(R.id.GreetingHeader);
        seller_name=hView.findViewById(R.id.seller_greetings);
        try {
            if(mAuth.getCurrentUser().getDisplayName()!=null){
                String S_name = "Hello: " + mAuth.getCurrentUser().getDisplayName();
                seller_name.setText(S_name);
            }
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }

        recyclerView=findViewById(R.id.userList2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list=new ArrayList<>();
        my_adapter =new My_adapter(this ,list);
        recyclerView.setAdapter(my_adapter);

        DatabaseReference myRef23;
        myRef23 = FirebaseDatabase.getInstance().getReference().child("users").child("ids").child(user.getUid());
        myRef23.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&& go){
                   try{
                       username= Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    number= Objects.requireNonNull(snapshot.child("number").getValue()).toString();}
                   catch (Exception e){
                       Log.d(TAG, e.getMessage());
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database= FirebaseDatabase.getInstance().getReference("products");
        database.addValueEventListener(new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(go) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);

                    list.add(product);
                }
                Collections.sort(list, (product, t1) -> Long.compare(t1.getTime(),product.getTime()));
                my_adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });


        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    my_adapter.filter(editText.getText().toString());
                    if(my_adapter.filter(editText.getText().toString())==0){

                        textview_none_2.setVisibility(View.VISIBLE);
                    }else{
                        textview_none_2.setVisibility(View.INVISIBLE);
                        go=false;

                    }


                return true;
            }
            return false;
        });
    }

    private void navigate(int mSelectedId) {

        if (mSelectedId == R.id.profile) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent i = new Intent(this, Profile.class);
            startActivity(i);
        }
        if (mSelectedId == R.id.support) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent i = new Intent(this, SupportActivity.class);
            startActivity(i);
        }
        if (mSelectedId == R.id.store) {
            if(username==null||number==null) {

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("")
                        .setMessage("complete profile")
                        .setPositiveButton("go to profile", (dialog, which) -> {
                            Intent i = new Intent(this, Profile.class);
                            startActivity(i);

                        })
                        .show();
            }else{
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent i = new Intent(this, StoreActivity.class);
            startActivity(i);}
        }

        if (mSelectedId == R.id.menuLogout1) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);
        int mSelectedId = menuItem.getItemId();
        navigate(mSelectedId);
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,MainActivity.class) );
            finish();
        }
    }

}
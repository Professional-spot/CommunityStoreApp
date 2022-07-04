package com.dzhosef.communitystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProductClicked extends AppCompatActivity {
    TextView priceView;
    TextView title, sellerView, amount_view, unit_view;
    private FirebaseAuth mAuth;
    String product, name, price, userid,task,unit,amount;
    String number;
    ImageView imageView;
    Uri photoUrl = null;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_clicked);

        imageView = findViewById(R.id.imageView_user_pic_profile_page);

        priceView = findViewById(R.id.main_image_View);
        title=findViewById(R.id.title);
        sellerView = findViewById(R.id.description);
        unit_view = findViewById(R.id.textView8);
        amount_view = findViewById(R.id.textView10);


        Toolbar toolbar = findViewById(R.id.toolbar_click);
        setSupportActionBar(toolbar);
        getData();
        setData();

        DatabaseReference myRef23;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        assert user != null;
        myRef23 = FirebaseDatabase.getInstance().getReference().child("users").child("ids").child(userid);
        myRef23.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    number= Objects.requireNonNull(snapshot.child("number").getValue()).toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button button=findViewById(R.id.button4);
        button.setOnClickListener(view -> {




            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+number));
            startActivity(intent);
        });
        loadUserInformation();


    }
    private void getData(){
        //if(getIntent().hasExtra("myImage")&&getIntent().hasExtra("data1")&&getIntent().hasExtra("data2")){
            try{
                product =getIntent().getStringExtra("data1");
            }catch (Exception ignored){}
            try{
                name =getIntent().getStringExtra("data2");
            }catch (Exception ignored){}
            try{
                    price =getIntent().getStringExtra("data3");
            }catch (Exception ignored){

            }
        try{
            userid =getIntent().getStringExtra("data4");
        }catch (Exception ignored){

        }
        try{
        task =getIntent().getStringExtra("data5");
    }catch (Exception ignored){

    }
        try{
            amount =getIntent().getStringExtra("data6");
        }catch (Exception ignored){

        }
        try{
            unit =getIntent().getStringExtra("data7");
        }catch (Exception ignored){
        }




            //Toast.makeText(this,"data",Toast.LENGTH_LONG).show();
        //}else{

         //   Toast.makeText(this,"no data",Toast.LENGTH_LONG).show();
       // }
    }
    private void setData(){
        try{
            title.setText(product);
        }catch (Exception ignored){}
        try{
            sellerView.setText(name);
        }catch (Exception ignored){}
        try{
            priceView.setText(price);
        }catch(Exception ignored){}
        try{
            amount_view.setText(amount);
        }catch (Exception ignored){}
        try{
           String unit1="/"+unit;
            unit_view.setText(unit1);
        }catch (Exception ignored){}



    }

    private void loadUserInformation() {

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseStorage.getInstance();
        assert user != null;
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("product/" +userid+"/"+ task + ".jpg");

        Task<Uri> task = profileImageRef.getDownloadUrl();
        task.addOnSuccessListener(uri -> {
            imageView.setVisibility(View.VISIBLE);

            photoUrl = uri;
            if (user.getPhotoUrl() != null) {
                try {
                    Glide.with(ProductClicked.this).load(photoUrl).into(imageView);
                } catch (Exception ignored) {

                }
            }
        }).addOnFailureListener(e -> imageView.setVisibility(View.INVISIBLE));


    }
    public void report(){
        database = FirebaseDatabase.getInstance();
        myRefer = database.getReference("reports");

        HelperClassReport helperClass = new HelperClassReport(user.getUid(), userid, task);

        try {
            myRefer.child(getAlphaNumericString()).setValue(helperClass);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_clicked, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


         if(item.getItemId()==R.id.menu_report) {
             //Toast.makeText(this, "e.getMessage()", Toast.LENGTH_SHORT).show();
             report();
         }
        return true;
    }
    static String getAlphaNumericString()
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(20);

        for (int i = 0; i < 20; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
package com.dzhosef.communitystore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(v -> {


            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+79855724362", null));
            startActivity(intent);

        });



    }
}
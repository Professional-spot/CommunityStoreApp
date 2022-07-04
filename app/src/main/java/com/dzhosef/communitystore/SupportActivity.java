package com.dzhosef.communitystore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        TextView tv = findViewById(R.id.text_s_s);
        tv.setText(Html.fromHtml(getString(R.string.email_us)));
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tv2 = findViewById(R.id.call_s_s);
        tv2.setText(Html.fromHtml(getString(R.string.call_us)));
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
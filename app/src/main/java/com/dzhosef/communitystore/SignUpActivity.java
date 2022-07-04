package com.dzhosef.communitystore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email,password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
        email=findViewById(R.id.email_sign_up_2);
        password=findViewById(R.id.password_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(){
        String email_sign_up=email.getText().toString().trim();
        String password_sign_up=password.getText().toString().trim();

        if(email_sign_up.isEmpty()){
            email.setError("enter email");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_sign_up).matches()){
            email.setError("enter valid email");
            email.requestFocus();
            return;
        }
        if(password_sign_up.isEmpty()){
            password.setError("enter password");
            password.requestFocus();
            return;
        }
        if(password_sign_up.length()<6){
            password.setError("minimum password character is 6");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email_sign_up,password_sign_up).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                FirebaseUser user = mAuth.getCurrentUser();

                assert user != null;
                user.sendEmailVerification().addOnCompleteListener(task1 -> Toast.makeText(SignUpActivity.this,"verification email sent",Toast.LENGTH_LONG).show());
                //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                finish();
            }else{
                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(getApplicationContext(),"User already registered",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.register){
            registerUser();
        }
        if(v.getId()==R.id.login){
            finish();
            startActivity(new Intent(this,MainActivity.class) );
        }

    }

    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,MainActivity.class) );
            finish();
        }
    }
}
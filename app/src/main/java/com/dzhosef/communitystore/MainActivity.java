package com.dzhosef.communitystore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    EditText email,password;

    ProgressBar progressBar;
    SignInButton sign_in_google;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sign_in).setOnClickListener(this);
        findViewById(R.id.signup).setOnClickListener(this);

        progressBar=findViewById(R.id.progressbar_sign_in);

        email=findViewById(R.id.email_sign_in);
        password=findViewById(R.id.password_sign_in);
        mAuth = FirebaseAuth.getInstance();
        sign_in_google =findViewById(R.id.sign_in_google);
        sign_in_google.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);












    }

    private void User_sign_in(){
        String email_sign_in=email.getText().toString().trim();
        String password_sign_in=password.getText().toString().trim();

        if(email_sign_in.isEmpty()){
            email.setError("enter email");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_sign_in).matches()){
            email.setError("enter valid email");
            email.requestFocus();
            return;
        }
        if(password_sign_in.isEmpty()){
            password.setError("enter password");
            password.requestFocus();
            return;
        }
        if(password_sign_in.length()<6){
            password.setError("minimum password character is 6");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email_sign_in,password_sign_in).addOnCompleteListener(task -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if(user==null){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "error try again", Toast.LENGTH_LONG).show();
                return;
            }

            if(task.isSuccessful()&&user.isEmailVerified()){

                Intent intent=new Intent(MainActivity.this,Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            }else{
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "error try again", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void User_sign_in_google(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }

    final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {


                    // There are no request codes
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }


            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information


                        Intent intent=new Intent(MainActivity.this,Home.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signup){
            startActivity(new Intent(this,SignUpActivity.class));
        }
        if(v.getId()==R.id.sign_in){
            User_sign_in();}
        if(v.getId()==R.id.sign_in_google){
            User_sign_in_google();}

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
//        assert user != null;
        if(mAuth.getCurrentUser() != null) {
            assert user != null;
            if (user.isEmailVerified()) {
                //finish();
                startActivity(new Intent(this, Home.class));
            }
        }

    }

}


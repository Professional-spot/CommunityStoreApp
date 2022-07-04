package com.dzhosef.communitystore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.exifinterface.media.ExifInterface;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Sell extends AppCompatActivity {
    Button sell_button;
    Spinner spinner_unit;
    private static final String[] paths ={"unit","lbs","oz","grams","kilogram","gallon","liter","cm3","spoon","cup"};
    private FirebaseAuth mAuth;
    EditText edittext1,edittext2,edittext3,amount_edit;
    String name,task,unit;
    Uri uri_profileImage;
    ImageView imageView;
    CardView cardView;
    FirebaseUser user;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        amount_edit =findViewById(R.id.editText_amount);
        spinner_unit =findViewById(R.id.spinner_unit);
        unit="unit";
        ArrayAdapter<String>adapter= new ArrayAdapter<>(Sell.this, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_unit.setAdapter(adapter);
        spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        unit="unit";
                        break;
                    case 1:
                        unit="lbs";
                        break;
                    case 2:
                        unit="oz";
                        break;
                    case 3:
                        unit="grams";
                        break;
                    case 4:
                        unit="kilogram";
                        break;
                    case 5:
                        unit="gallon";
                        break;
                    case 6:
                        unit="liter";
                        break;
                    case 7:
                        unit="cm3";
                        break;
                    case 8:
                        unit="spoon";
                        break;
                    case 9:
                        unit="cup";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imageView = findViewById(R.id.imageView2_Update);
        Button button=findViewById(R.id.button6);
        button.setOnClickListener(view -> showImageChooser());
        cardView=findViewById(R.id.cardUpdate);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseStorage.getInstance();
        assert user != null;

        int n = 25;

        // Get and display the alphanumeric string
        task=getAlphaNumericString(n);

        sell_button =findViewById(R.id.button2);
        edittext1=findViewById(R.id.editTextTextPersonName);
        edittext2=findViewById(R.id.editTextTextPersonName2);
        edittext3=findViewById(R.id.editTextNumber);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");
        DatabaseReference myRef23 = FirebaseDatabase.getInstance().getReference().child("users").child("ids").child(user.getUid());
        myRef23.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name= Objects.requireNonNull(snapshot.child("name").getValue()).toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sell_button.setOnClickListener(view -> {

            if (edittext1.getText().toString().equals("")) {
                edittext1.setError("enter name");
                edittext1.requestFocus();
                return;
            }
            if (edittext1.getText().toString().length()>20) {
                edittext1.setError("name too long");
                edittext1.requestFocus();
                return;
            }
            if(amount_edit.getText().toString().equals("")){
                amount_edit.setError("enter amount");
                amount_edit.requestFocus();
                return;
            }
            if(amount_edit.getText().toString().length()>4){
                amount_edit.setError("amount error,  (9999 max)");
                amount_edit.requestFocus();
                return;
            }
            String product_name=edittext1.getText().toString();

            long seconds = System.currentTimeMillis() / 1000L;

            if (edittext2.getText().toString().equals("")) {
                edittext2.setText("0");
            }
            if (edittext2.getText().toString().length()>4) {
                edittext2.setError("3 digit number");
                edittext2.requestFocus();
                return;

            }
            if (edittext3.getText().toString().equals("")) {

                edittext3.setText(R.string.cents);

            }
            if (edittext3.getText().toString().length()>2) {
                edittext3.setError("2 digit number");
                edittext3.requestFocus();
                return;

            }

                String cents=(edittext3.getText().toString());
                Double price=Double.parseDouble(edittext2.getText() +"."+cents);
                HelperClass helperClass = new HelperClass(name,product_name, price,user.getUid(),task,seconds, amount_edit.getText().toString(),unit);
                try {
                    myRef.child(task).setValue(helperClass);
                }catch (Exception ignored){

                }


           if(bitmap != null) {
               uploadImageToFirebaseStorage(task, user.getUid(), bitmap);
           }


            finish();







        });


    }

    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

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

    public void showImageChooser() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }
    final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    // doSomeOperations();
                    Intent data = result.getData();
                    uri_profileImage = Objects.requireNonNull(data).getData();
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_profileImage);
                        ExifInterface ei = new ExifInterface(getContentResolver().openInputStream(uri_profileImage));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        switch(orientation) {

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(bitmap, 90);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(bitmap, 180);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(bitmap, 270);
                                break;
                        }

                        imageView.setImageBitmap(bitmap);

                        cardView.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Sell.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            });

    private void uploadImageToFirebaseStorage(String text,String seller_id, Bitmap bitmap) {

        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("product/"+seller_id+"/" + text + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int maxHeight = 2000;
        int maxWidth = 2000;
        float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        if (uri_profileImage != null) {


            profileImageRef.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> Toast.makeText(Sell.this,"uploaded",Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e -> Toast.makeText(Sell.this,"error",Toast.LENGTH_LONG).show());
        }

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,MainActivity.class) );
            finish();
        }
    }


}
package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.R;
import com.example.capstone_devwolfs_get_a_pet.classes.Adopter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class AdopterAuthActivity extends AppCompatActivity {

    EditText aName, aEmail,aPassword,aPhone,aAddress,aDescription;
    TextView alogin;
    Button save;
    ImageView adopterImage;
    Uri adopterImageUri;
    FloatingActionButton fabAdopter;
    Bitmap abitmap;


    public static final String TAG = "test";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopter_auth);
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        aName = findViewById(R.id.adopterName);
        aEmail = findViewById(R.id.adopterEmail);
        aPassword = findViewById(R.id.adopterPassword);
        aDescription = findViewById(R.id.adopterDescription);
        aPhone = findViewById(R.id.adopterPhone);
        aAddress = findViewById(R.id.adopterAddress);

        save = findViewById(R.id.adoptersaveBtn);
        alogin = findViewById(R.id.adopterLoginRedirect);
        adopterImage = findViewById(R.id.profile_adopterImage);
        fabAdopter = findViewById(R.id.adopterProfileImagefloatingActionButton);

        fabAdopter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImagePicker.with(AdopterAuthActivity.this)
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtoFireBase();
                /*

                String name = aName.getText().toString().trim();
                String email = aEmail.getText().toString().trim();
                String password = aPassword.getText().toString().trim();
                String phone = aPhone.getText().toString().trim();
                String address = aAddress.getText().toString().trim();
                String description = aDescription.getText().toString().trim();

                Adopter adopter = new Adopter(name,email,phone,address,description,password);

                db.collection("Adopters").add(adopter);
                clearFields();
                Toast.makeText(getApplicationContext(),"Adopter Added",Toast.LENGTH_LONG).show();

                 */
            }

            private void uploadtoFireBase() {
                ProgressDialog dialog = new ProgressDialog(AdopterAuthActivity.this);
                dialog.setTitle("Creating adopter profile...");
                dialog.show();
                String name = aName.getText().toString().trim();
                String email = aEmail.getText().toString().trim();
                String password = aPassword.getText().toString().trim();
                String phone = aPhone.getText().toString().trim();
                String address = aAddress.getText().toString().trim();
                String description = aDescription.getText().toString().trim();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://capstone-100bc.appspot.com/");
                StorageReference imageName = storageRef.child("adopterProfileImage"+System.currentTimeMillis()+".jpg");

                imageName.putFile(adopterImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();
                                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Adopter adopter = new Adopter(name,email,phone,address,description,password,uri.toString());

                                        db.collection("Adopters").add(adopter);
                                        clearFields();
                                        Toast.makeText(getApplicationContext(),"Adopter Added",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                float percent =(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                dialog.setMessage("uploaded: " +(int)percent +"%");
                            }
                        });

            }
        });

        alogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AdopterLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adopterImageUri = data.getData();
        try{
            InputStream inputStream = getContentResolver().openInputStream(adopterImageUri);
            abitmap= BitmapFactory.decodeStream(inputStream);
            adopterImage.setImageBitmap(abitmap);

        } catch (Exception e)
        {

        }
    }
    private void clearFields(){

        aName.setText("");
        aEmail.setText("");
        aPhone.setText("");
        aAddress.setText("");
        aDescription.setText("");
        aPassword.setText("");
    }
}
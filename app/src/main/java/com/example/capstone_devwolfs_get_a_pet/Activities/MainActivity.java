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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.R;
import com.example.capstone_devwolfs_get_a_pet.classes.Shelter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    EditText sName, sEmail,sPassword,sPhone,sAddress,sDescription;
    Button save;
    ImageView shelterImage;
    CircleImageView shelterDefImag;
    Uri shelterImageUri;
    FloatingActionButton fabShelter;
    Bitmap sbitmap;

    public static final String TAG = "test";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    DocumentReference ref = db.collection("Shelters").document();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        sName = findViewById(R.id.shelterName);
        sEmail = findViewById(R.id.shelterEmail);
        sPassword = findViewById(R.id.shelterPassword);
        sDescription = findViewById(R.id.shelterDescription);
        sPhone = findViewById(R.id.shelterPhone);
        sAddress = findViewById(R.id.shelterAddress);

        save = findViewById(R.id.shelterSaveBtn);

        shelterImage = findViewById(R.id.profile_shelterImage);
        fabShelter = findViewById(R.id.shelterImagefloatingActionButton);

        shelterDefImag = findViewById(R.id.profile_shelterImage);

        String linkDef = "https://firebasestorage.googleapis.com/v0/b/capstone-100bc.appspot.com/o/shelter.png?alt=media&token=abc6708a-8b6e-40f4-8fe9-95f1964e44c2";
        Picasso.get().load(linkDef).into(shelterImage);

        fabShelter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImagePicker.with(MainActivity.this)
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
                String name = sName.getText().toString().trim();
                String email = sEmail.getText().toString().trim();
                String password = sPassword.getText().toString().trim();
                String phone = sPhone.getText().toString().trim();
                String address = sAddress.getText().toString().trim();
                String description = sDescription.getText().toString().trim();

                String id = ref.getId();

                Shelter shelter = new Shelter(id,name,email,phone,address,description,password);

                db.collection("Shelters").add(shelter);

                clearFields();
                Toast.makeText(getApplicationContext(),"Shelter Added",Toast.LENGTH_LONG).show();
            */
            }


            private void uploadtoFireBase() {
                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Creating shelter Profile...");
                dialog.show();
                String name = sName.getText().toString().trim();
                String email = sEmail.getText().toString().trim();
                String password = sPassword.getText().toString().trim();
                String phone = sPhone.getText().toString().trim();
                String address = sAddress.getText().toString().trim();
                String description = sDescription.getText().toString().trim();
                String id = ref.getId();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://capstone-100bc.appspot.com/");
                StorageReference imageName = storageRef.child("shelterProfileImage"+System.currentTimeMillis()+".jpg");

                imageName.putFile(shelterImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();
                                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Shelter shelter = new Shelter(id,name,email,phone,address,description,password,uri.toString());
                                        db.collection("Shelters").add(shelter);
                                        clearFields();
                                        Toast.makeText(getApplicationContext(),"Shelter  Added",Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                dialog.setMessage("uploaded: " +(int)percent +"%");
                            }
                        });

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shelterImageUri = data.getData();
        try{
            InputStream inputStream = getContentResolver().openInputStream(shelterImageUri);
            sbitmap= BitmapFactory.decodeStream(inputStream);
            shelterImage.setImageBitmap(sbitmap);

        } catch (Exception e)
        {

        }
    }

    private void clearFields(){
        sName.setText("");
        sEmail.setText("");
        sPhone.setText("");
        sAddress.setText("");
        sDescription.setText("");
        sPassword.setText("");
    }
}
package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.R;
import com.example.capstone_devwolfs_get_a_pet.classes.Pet;
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
import java.util.ArrayList;
import java.util.List;

public class addPetActivity extends AppCompatActivity {

    EditText pName, pBreed,pDescription;
    Spinner spinType,spinSize;
    ImageView petImage;
    Uri petImageUri;
    FloatingActionButton FabPet;
    Button save;
    Bitmap bitmap;
    String selectedType,selectedSize;

    public static final String TAG = "test";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
//
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        pName = findViewById(R.id.petNameEdited);
        pDescription = findViewById(R.id.petDescriptionNew);
        pBreed = findViewById(R.id.petBreedNew);
        save = findViewById(R.id.petAddBtn);
        petImage = findViewById(R.id.petImageNew);
        FabPet = findViewById(R.id.floatingActionButtonPetEdit);
        spinType = findViewById(R.id.spinnerType);
        spinSize = findViewById(R.id.spinnerSize);

        //Type options
        List<String> types = new ArrayList<String>();
        types.add("Cat");
        types.add("Dog");
        types.add("Bird");

        //Sizes options
        List<String> sizes = new ArrayList<String>();
        sizes.add("Small");
        sizes.add("Medium");
        sizes.add("Big");

        //Spinner Adapters
        ArrayAdapter<String> dataAdapterTypeSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        ArrayAdapter<String> dataAdapterSizeSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizes);

        //Spinners Resources
        dataAdapterTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterSizeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapters to spinners
        spinType.setAdapter(dataAdapterTypeSpinner);
        spinSize.setAdapter(dataAdapterSizeSpinner);

        FabPet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImagePicker.with(addPetActivity.this)
                        .cropSquare()	    	//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtoFireBase();
            }
        });

        //Event listeners for Spinners
        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = spinType.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSize = spinSize.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void uploadtoFireBase() {
        ProgressDialog dialog = new ProgressDialog(addPetActivity.this);
        dialog.setTitle("Adding your Pet for adoption...");
        dialog.show();
        String shelterid = ShelterLoginActivity.shelterUsID;
        String name = pName.getText().toString().trim();
        String breed = pBreed.getText().toString().trim().toUpperCase();
        String type = selectedType;
        String description = pDescription.getText().toString().trim();
        String size = selectedSize;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://capstone-100bc.appspot.com/");
        StorageReference imageName = storageRef.child("petimage"+System.currentTimeMillis()+".jpg");

        imageName.putFile(petImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Pet pet = new Pet(name,shelterid,breed,type,size,description,uri.toString());
                                db.collection("Pets").add(pet);
                                clearFields();
                                Toast.makeText(getApplicationContext(),"Pet Added",Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        petImageUri = data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(petImageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                petImage.setImageBitmap(bitmap);
            } catch (Exception e) {

            }
       }

    void clearFields(){
        pName.setText("");
        pBreed.setText("");
        pDescription.setText("");
        petImage.setImageBitmap(null);
    }

}
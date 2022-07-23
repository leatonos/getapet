package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdopterProfileActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferences;
    EditText adopterName,adopterEmail,adopterPhone,adopterAddress,adopterDescription;
    Button saveChangesBtn;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopter_profile);

        sharedPreferences = getSharedPreferences("Adopter",  Context.MODE_PRIVATE);
        String AdopterId = PersistentData.getAdopterId(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference user = firebaseFirestore.collection("Adopters").document(AdopterId);

        //Fields
        adopterName = findViewById(R.id.adopterNameTv);
        adopterEmail = findViewById(R.id.adopterEmailTv);
        adopterPhone = findViewById(R.id.adopterPhoneTv);
        adopterAddress = findViewById(R.id.adopterAddressTv);
        adopterDescription = findViewById(R.id.adopterDescriptionTv);

        //Button
        saveChangesBtn = findViewById(R.id.buttonSaveChangesAdopter);

        adopterName.setText(sharedPreferences.getString("Name","Adopter Name"));
        adopterEmail.setText(sharedPreferences.getString("Email","Adopter Email"));
        adopterPhone.setText(sharedPreferences.getString("Phone","Adopter Phone"));
        adopterAddress.setText(sharedPreferences.getString("Address","Adopter Address"));
        adopterDescription.setText(sharedPreferences.getString("Description","Adopter Description"));

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newName = adopterName.getText().toString().trim();
                String newAddress = adopterAddress.getText().toString().trim();
                String newEmail = adopterEmail.getText().toString().trim();
                String newPhone = adopterPhone.getText().toString().trim();
                String newDescription = adopterDescription.getText().toString().trim();

                user.update(
                        "adopterAddress", newAddress,
                        "adopterDescription", newDescription,
                        "adopterEmail", newEmail,
                        "adopterPhone", newPhone,
                        "adopterName", newName
                        );

                sharedPreferences = getSharedPreferences("Adopter",  Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("Address", newAddress);
                editor.putString("Description", newDescription);
                editor.putString("Email", newEmail);
                editor.putString("Name", newName);
                editor.putString("Phone", newPhone);

                Toast.makeText(getApplicationContext(), "Information Edited", Toast.LENGTH_SHORT).show();

                editor.commit();

            }
        });

    }
}
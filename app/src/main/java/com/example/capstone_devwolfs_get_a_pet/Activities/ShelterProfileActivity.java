package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShelterProfileActivity extends AppCompatActivity {

    EditText name,email,phone,address,descp,password;
    Button saveButton;

    SharedPreferences sharedPreferences;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_profile);

        sharedPreferences = getSharedPreferences("Shelter", Context.MODE_PRIVATE);
        String ShelterId = PersistentData.getShelterId(this);
        DocumentReference user = db.collection("Shelters").document(ShelterId);

        //Fields
        name = findViewById(R.id.shelterNameTv);
        email = findViewById(R.id.shelterEmailTv);
        phone = findViewById(R.id.shelterPhoneTv);
        address = findViewById(R.id.shelterAddressTv);
        descp = findViewById(R.id.shelterDescriptionTv);

        //Button
        saveButton = findViewById(R.id.saveChangesShelter);

        //Get Info
        name.setText(sharedPreferences.getString("Name","Shelter Name"));
        email.setText(sharedPreferences.getString("Email","Shelter Email"));
        phone.setText(sharedPreferences.getString("Phone","Shelter Phone"));
        address.setText(sharedPreferences.getString("Address","Shelter Address"));
        descp.setText(sharedPreferences.getString("Description","Shelter Description"));

        //Save information

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newName = name.getText().toString().trim();
                String newAddress = address.getText().toString().trim();
                String newEmail = email.getText().toString().trim();
                String newPhone = phone.getText().toString().trim();
                String newDescription = descp.getText().toString().trim();


                user.update(
                        "shelterAddress", newAddress,
                        "shelterDescription", newDescription,
                        "shelterEmail", newEmail,
                        "shelterPhone", newPhone,
                        "shelterName", newName
                );

                sharedPreferences = getSharedPreferences("Shelter",  Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("Address",newAddress);
                editor.putString("Description",newDescription);
                editor.putString("Email",newEmail);
                editor.putString("Name", newName);
                editor.putString("Phone", newPhone);

                editor.commit();

            }
        });





    }


}
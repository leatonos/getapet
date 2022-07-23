package com.example.capstone_devwolfs_get_a_pet.Activities.Shelters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class EditPetShelter extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    EditText petName,petBreed,petType,petSize,petDescription;
    ImageView petPhoto;
    Button updatePetBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_shelter);

        //Retrieves the pet's Id you clicked
        String petID = (String) getIntent().getExtras().get("editedPetID");

        //Selects the Pets in the database
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference selectedPet = firebaseFirestore.collection("Pets").document(petID);

        petName = findViewById(R.id.petNameEdited);
        petBreed = findViewById(R.id.petBreedEdited);
        petType = findViewById(R.id.petTypeEdited);
        petSize = findViewById(R.id.petSizeEdited);
        petDescription = findViewById(R.id.petDescriptionEdited);
        petPhoto = findViewById(R.id.petImageEdited);
        updatePetBtn = findViewById(R.id.petEditBtn);

        //Loads the information from the pet into the form
        selectedPet.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        petName.setText(document.getString("petName"));
                        petBreed.setText(document.getString("breed"));
                        petType.setText(document.getString("type"));
                        petSize.setText(document.getString("size"));
                        petDescription.setText(document.getString("description"));

                        String imageLink = document.getString("petImage");
                        Picasso.get().load(imageLink).into(petPhoto);

                    } else {
                        Log.d("DATABASE ERROR", "No such document");
                    }
                } else {
                    Log.d("DATABASE ERROR", "get failed with ", task.getException());
                }

            }

        });

        //Update Pet Button
        updatePetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPet.update(
                        "petName",petName.getText().toString().trim(),
                        "breed",petBreed.getText().toString().trim(),
                        "type",petType.getText().toString().trim(),
                        "size",petSize.getText().toString().trim(),
                        "description",petDescription.getText().toString().trim()
                );

                Toast.makeText(getApplicationContext(), "Pet Edited", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), ShowAllPetsShelter.class);
                startActivity(intent);

            }
        });



    }

}
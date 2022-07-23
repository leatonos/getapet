package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.Activities.Shelters.ShowAllPetsShelter;
import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.squareup.picasso.Picasso;

public class ShelterDashboardActivity extends AppCompatActivity {


    Button addPet,viewProfile,seeAllPets;
    ImageView ShelterPhoto;
    TextView shelterLogoff,shelterNameTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_dashboard);

        //Loads Shelter Image
        String imageLink = PersistentData.getShelterImage(getApplicationContext());
        ShelterPhoto = findViewById(R.id.shelterPhoto);
        Picasso.get().load(imageLink).into(ShelterPhoto);


        addPet = findViewById(R.id.addPetRedirect);
        viewProfile = findViewById(R.id.viewProfileBtn);
        seeAllPets = findViewById(R.id.allPetsShelterBtn);
        shelterLogoff = findViewById(R.id.logoutBtnShelter);
        shelterNameTV = findViewById(R.id.ShelterUserName);


        shelterNameTV.setText(PersistentData.getShelterName(this));

        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), addPetActivity.class);
                startActivity(intent);
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShelterProfileActivity.class);
                startActivity(intent);
            }
        });

        seeAllPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowAllPetsShelter.class);
                startActivity(intent);
            }
        });

        shelterLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersistentData.logoutAdopter(v.getContext());
                PersistentData.logoutShelter(v.getContext());
                Intent intent = new Intent(ShelterDashboardActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


    }
}
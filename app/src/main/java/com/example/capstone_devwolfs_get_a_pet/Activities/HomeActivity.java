package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.R;

public class HomeActivity extends AppCompatActivity {

    Button regShelter,regAdopter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        regShelter = findViewById(R.id.regShelter_btn);
        regAdopter = findViewById(R.id.reg_adopter_btn);

        regShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShelterLoginActivity.class);
                startActivity(intent);
            }
        });

        regAdopter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AdopterLoginActivity.class);
                startActivity(intent);
            }
        });

        if(PersistentData.getLoggedStatusAdopter(this)){
            Intent intent = new Intent(HomeActivity.this, AdopterDashboardActivity.class);
            startActivity(intent);
        }

        if(PersistentData.getLoggedStatusShelter(this)){
            Intent intent = new Intent(HomeActivity.this, ShelterDashboardActivity.class);
            startActivity(intent);
        }

    }
}
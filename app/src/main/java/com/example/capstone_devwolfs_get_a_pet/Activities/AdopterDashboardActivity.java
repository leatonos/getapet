package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.Activities.Adopters.FindPetActivity;
import com.example.capstone_devwolfs_get_a_pet.Activities.Adopters.FindShelterActivity;
import com.example.capstone_devwolfs_get_a_pet.Activities.Adopters.Wishlist;
import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.squareup.picasso.Picasso;

public class AdopterDashboardActivity extends AppCompatActivity {

    ImageView AdopterPhoto;
    TextView logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopter_dashboard);

        logOutBtn = findViewById(R.id.logOutAdopter);

        //Loads adopter Image
        String imageLink = PersistentData.getAdopterImage(getApplicationContext());
        AdopterPhoto = findViewById(R.id.imageViewAdopterPetFinder);
        Picasso.get().load(imageLink).into(AdopterPhoto);
        Log.d("IMAGE", "onCreate: "+imageLink);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersistentData.logoutAdopter(v.getContext());
                PersistentData.logoutShelter(v.getContext());
                Intent intent = new Intent(AdopterDashboardActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    public void findShelter(View view) {
        Intent intent = new Intent(AdopterDashboardActivity.this, FindShelterActivity.class);
        startActivity(intent);
    }

    public void findPet(View view) {
        Intent intent = new Intent(AdopterDashboardActivity.this, FindPetActivity.class);
        startActivity(intent);
    }

    public void editProfile(View view) {
        Intent intent = new Intent(AdopterDashboardActivity.this, AdopterProfileActivity.class);
        startActivity(intent);
    }

    public void openWishlist(View view){
        Intent intent = new Intent(AdopterDashboardActivity.this, Wishlist.class);
        startActivity(intent);
    }
}
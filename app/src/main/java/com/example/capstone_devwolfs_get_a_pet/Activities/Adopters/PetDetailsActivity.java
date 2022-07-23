package com.example.capstone_devwolfs_get_a_pet.Activities.Adopters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class PetDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseFirestore firebaseFirestore;

    ImageView petImage, productImage;
    private GoogleMap mapShelterDetail;
    TextView textPetName, textPetDescription, textBreed, textPetSize;
    TextView textShelterName, textShelterDescription, petNameTxt;
    TextView productTextName, buyLink;
    TextView wishListBtn;
    String productImageLink, productBuyLink, productStringName, shelterAddress, shelterEmail, shelterPhone;

    LinearLayout sponsorArea;
    public Button callBtn, emailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapShelter);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //Retrieve information form your last choice
        String shelterID = (String) getIntent().getExtras().get("shelterId");
        String petID = (String) getIntent().getExtras().get("petId");
        String petBreed = (String) getIntent().getExtras().get("petBreed");
        String userID = PersistentData.getAdopterId(getApplicationContext());

        //Product Details
        sponsorArea = findViewById(R.id.sponsorBox);
        productImage = findViewById(R.id.productImage);
        productTextName = findViewById(R.id.productName);
        buyLink = findViewById(R.id.buyLink);

        sponsorArea.setVisibility(View.GONE);

        //Pet Texts
        textPetName = findViewById(R.id.petNameProfile);
        textPetDescription = findViewById(R.id.petDescProfile);
        textBreed = findViewById(R.id.petBreedProfile);
        textPetSize = findViewById(R.id.petSizeProfile);
        petNameTxt = findViewById(R.id.backToProfile);

        //Shelter Texts
        textShelterName = findViewById(R.id.shelterNamePetProfile);
        textShelterDescription = findViewById(R.id.textShelterDescPetProfile);

        //Button
        callBtn = findViewById(R.id.buttonCall);
        emailBtn = findViewById(R.id.buttonEmail);

        //Images
        petImage = findViewById(R.id.imageViewPetProfile);

        //Database
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference selectedPet = firebaseFirestore.collection("Pets").document(petID);
        DocumentReference selectedShelter = firebaseFirestore.collection("Shelters").document(shelterID);
        DocumentReference user = firebaseFirestore.collection("Adopters").document(userID);
        CollectionReference sponsorship = firebaseFirestore.collection("Sponsorship");

        //Loads Sponsorship
        sponsorship.whereEqualTo("petBreed",petBreed).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.size() != 0){
                    sponsorArea.setVisibility(View.VISIBLE);

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        productImageLink = documentSnapshot.getString("productImage");
                        productBuyLink = documentSnapshot.getString("productLink");
                        productStringName = documentSnapshot.getString("productName");

                        Picasso.get().load(productImageLink).into(productImage);
                        productTextName.setText(productStringName);


                        buyLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productBuyLink));
                                startActivity(browserIntent);
                            }
                        });

                    }
                }

            }

        });

        //Gets the information from the Selected pet and writes into the Screen
        selectedPet.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        textPetName.setText(document.getString("petName"));
                        petNameTxt.setText(document.getString("petName"));
                        textBreed.setText(document.getString("breed"));
                        textPetSize.setText(document.getString("size"));
                        textPetDescription.setText(document.getString("description"));

                        String imageLink = document.getString("petImage");
                        Picasso.get().load(imageLink).into(petImage);

                    } else {
                        Log.d("DATABASE ERROR", "No such document");
                    }
                } else {
                    Log.d("DATABASE ERROR", "get failed with ", task.getException());
                }
            }
        });

        //Gets the information from the pet's shelter and writes into the Screen
        selectedShelter.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        textShelterName.setText(document.getString("shelterName"));
                        textShelterDescription.setText(document.getString("shelterDescription"));
                        shelterAddress = document.getString("shelterAddress");

                        addShelterMarker(shelterAddress);
                        shelterEmail = document.getString("shelterEmail");
                        shelterPhone = document.getString("shelterPhone");


                    } else {
                        Log.d("DATABASE ERROR", "No such document");
                    }
                } else {
                    Log.d("DATABASE ERROR", "get failed with ", task.getException());
                }
            }
        });

        wishListBtn = findViewById(R.id.addWishListBtn);

        wishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get the Wishlist in the database
                user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                if(document.contains("wishlist") && document.getString("wishlist") != ""){

                                    String updatedWishlist = document.getString("wishlist");
                                    updatedWishlist += ","+petID;

                                    user.update("wishlist", updatedWishlist);
                                    PersistentData.updateAdopterWishlist(getApplicationContext(),updatedWishlist);
                                    Toast.makeText(getApplicationContext(), "Pet Added to the Wishlist", Toast.LENGTH_LONG).show();

                                }else{
                                    user.update("wishlist", petID);
                                    Toast.makeText(getApplicationContext(), "Pet Added to the Wishlist", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d("DATABASE ERROR", "No such document");
                                Toast.makeText(getApplicationContext(), "User not Found", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else {
                            Log.d("DATABASE ERROR", "get failed with ", task.getException());
                            Toast.makeText(getApplicationContext(), "Connection Problem, check your internet", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                });
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", shelterPhone, null));
                startActivity(intent);

            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String petNameEmail = textPetName.getText().toString().trim();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{shelterEmail});
                email.putExtra(Intent.EXTRA_SUBJECT, "I am interested about " + petNameEmail);
                //email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });


    }

    public void addShelterMarker(String shelterAddress){
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        //Toast.makeText(getApplicationContext(), "Results: "+ shelterAddress, Toast.LENGTH_LONG).show();

        // Setting the position for the marker
        LatLng coordinates = getLocationFromAddress(getApplicationContext(),shelterAddress);
        Toast.makeText(getApplicationContext(), "Latitute = "+ coordinates.latitude, Toast.LENGTH_LONG).show();


        //Toast.makeText(getApplicationContext(), "Results: "+ coordinates.toString(), Toast.LENGTH_LONG).show();

        if (coordinates != null){

            LatLng newCoords = new LatLng(coordinates.latitude, coordinates.longitude);

            markerOptions.position(coordinates);
            markerOptions.title(textShelterName.getText().toString());

            mapShelterDetail.moveCamera(CameraUpdateFactory.newLatLngZoom(newCoords, 15));
            mapShelterDetail.addMarker(markerOptions);

        }

    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

        mapShelterDetail = googleMap;

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                p1 = null;
                Toast.makeText(getApplicationContext(), "Results: Address is null", Toast.LENGTH_LONG).show();
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


}
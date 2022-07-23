package com.example.capstone_devwolfs_get_a_pet.Activities.Adopters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_devwolfs_get_a_pet.Models.PetInShelterModel;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ShelterDetailActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView petsListShelterMap;
    private FirestoreRecyclerAdapter adapter;

    TextView shelterNameText,shelterDescriptionText;
    ImageView shelterImageDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        shelterNameText = findViewById(R.id.shelterNameDetail);
        shelterDescriptionText = findViewById(R.id.shelterDescriptionDetail);
        shelterImageDetail = findViewById(R.id.imageDetailShelter);

        String  shelterName = (String) getIntent().getExtras().get("shelterName");
        String  shelterEmail = (String) getIntent().getExtras().get("shelterEmail");
        String  shelterAddress = (String) getIntent().getExtras().get("shelterAddress");
        String  shelterDescription = (String) getIntent().getExtras().get("shelterDes");
        String  shelterID = (String) getIntent().getExtras().get("shelterId");
        String  shelterImage = (String) getIntent().getExtras().get("shelterImage");

        shelterNameText.setText(shelterName);
        shelterDescriptionText.setText(shelterDescription);


        Picasso.get().load(shelterImage).into(shelterImageDetail);

        firebaseFirestore = FirebaseFirestore.getInstance();
        petsListShelterMap = findViewById(R.id.petsInShelterDetail);


        //This Query gets all the pets in that Shelter
        Query query = firebaseFirestore.collection("Pets").whereEqualTo("shelterId",shelterID);
        Toast.makeText(getApplicationContext(), query.toString(), Toast.LENGTH_LONG).show();


        //This is the code that builds the cells of each pet
        FirestoreRecyclerOptions<PetInShelterModel> options = new FirestoreRecyclerOptions.Builder<PetInShelterModel>()
                .setQuery(query, PetInShelterModel.class)
                .build();

        //Adapter
        adapter = new FirestoreRecyclerAdapter<PetInShelterModel, PetsViewHolderMap>(options) {

            @NonNull
            @NotNull
            @Override
            public PetsViewHolderMap onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_in_shelter_for_adopters, parent, false);
                return new PetsViewHolderMap(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull PetsViewHolderMap holder, int position, @NonNull @NotNull PetInShelterModel model) {

                holder.petNameMap.setText(model.getPetName());
                holder.petTypeMap.setText(model.getType());
                Picasso.get().load(model.getPetImage()).into(holder.petPhotoMap);

                holder.petPhotoMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ShelterDetailActivity.this, PetDetailsActivity.class);

                        intent.putExtra("petId",model.getPetID());
                        intent.putExtra("shelterId",model.getShelterId());
                        intent.putExtra("petBreed", model.getBreed());

                        startActivity(intent);

                    }
                });

            }
        };

        petsListShelterMap.setLayoutManager(new LinearLayoutManager(this));
        petsListShelterMap.setAdapter(adapter);

    }

    //View Holder
    private class PetsViewHolderMap extends RecyclerView.ViewHolder {

        private TextView petNameMap;
        private TextView petTypeMap;
        private ImageView petPhotoMap;

        public PetsViewHolderMap(@NonNull @NotNull View itemView) {
            super(itemView);

            petNameMap = itemView.findViewById(R.id.petNameMapShelter);
            petTypeMap = itemView.findViewById(R.id.petTypeMapShelter);
            petPhotoMap = itemView.findViewById(R.id.petImageMapShelter);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

}
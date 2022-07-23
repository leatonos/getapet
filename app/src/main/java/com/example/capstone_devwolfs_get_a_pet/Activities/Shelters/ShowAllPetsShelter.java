package com.example.capstone_devwolfs_get_a_pet.Activities.Shelters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.Models.PetInShelterModel;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ShowAllPetsShelter extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView petsListShelter;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_pets_shelter);

        sharedPreferences = getSharedPreferences("Shelter", Context.MODE_PRIVATE);

        String thisShelterID = PersistentData.getShelterId(this);
        Toast.makeText(getApplicationContext(), thisShelterID, Toast.LENGTH_LONG).show();

        firebaseFirestore = FirebaseFirestore.getInstance();
        petsListShelter = findViewById(R.id.petListShelter);

        //This is the query
        Query query = firebaseFirestore.collection("Pets").whereEqualTo("shelterId",thisShelterID);

        //This is the code that builds the cells of each pet
        FirestoreRecyclerOptions<PetInShelterModel> options = new FirestoreRecyclerOptions.Builder<PetInShelterModel>()
                .setQuery(query, PetInShelterModel.class)
                .build();

        //Adapter
        adapter =  new FirestoreRecyclerAdapter<PetInShelterModel, PetsViewHolder>(options) {

            @NonNull
            @NotNull
            @Override
            public PetsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_in_shelter,parent, false);
                return new PetsViewHolder(view);



            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull PetsViewHolder holder, int position, @NonNull @NotNull PetInShelterModel model) {
                holder.petName.setText(model.getPetName());
                Picasso.get().load(model.getPetImage()).into(holder.petPhoto);

                holder.editPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), EditPetShelter.class);
                        intent.putExtra("editedPetID",model.getPetID());
                        startActivity(intent);

                    }
                });


                holder.deletePet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "Pet Deleted", Toast.LENGTH_LONG).show();
                        firebaseFirestore.collection("Pets").document(model.getPetID()).delete();

                    }
                });

            }
        };

        //petsListShelter.setHasFixedSize(true);
        petsListShelter.setLayoutManager(new LinearLayoutManager(this));
        petsListShelter.setAdapter(adapter);

        Toast.makeText(getApplicationContext(), "Pets :"+adapter.getItemCount(), Toast.LENGTH_LONG).show();
    }

    //View Holder
    private class PetsViewHolder extends RecyclerView.ViewHolder {

        private TextView petName;
        private ImageView petPhoto;
        private ImageButton editPet,deletePet;

        public PetsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            petName = itemView.findViewById(R.id.petNameShelter);
            petPhoto = itemView.findViewById(R.id.petImageWishList);
            editPet = itemView.findViewById(R.id.editPetShelter);
            deletePet = itemView.findViewById(R.id.deletePetShelter);

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


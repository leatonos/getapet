package com.example.capstone_devwolfs_get_a_pet.Activities.Adopters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

public class FindPetActivity extends AppCompatActivity {


    private FirebaseFirestore firebaseFirestore;
    private RecyclerView findAllPetsGridRV;
    private FirestoreRecyclerAdapter adapter;
    private Spinner sizeSpinner, typeSpinner;
    private ImageView profilePic;
    public String selectedType, selectedSize;
    public List<String> selectedSizes;
    public EditText searchTextView;
    public SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pet);

        firebaseFirestore = FirebaseFirestore.getInstance();
        findAllPetsGridRV = findViewById(R.id.gridRecyclerViewFindPets);
        typeSpinner = findViewById(R.id.spinnerTypes);
        sizeSpinner = findViewById(R.id.spinnerSizes);
        profilePic = findViewById(R.id.imageViewAdopterPetFinder);
        //searchTextView = findViewById(R.id.searchTV);
        searchView = findViewById(R.id.searchV);


        //Sizes types
        List<String> types = new ArrayList<String>();
        types.add("All types");
        types.add("Cat");
        types.add("Dog");
        types.add("Bird");
        selectedType = "All types";

        //Sizes options
        List<String> sizes = new ArrayList<String>();
        sizes.add("All sizes");
        sizes.add("Small");
        sizes.add("Medium");
        sizes.add("Big");
        selectedSize = "All sizes";

        //Spinner Adapters
        ArrayAdapter<String> dataAdapterTypeSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        ArrayAdapter<String> dataAdapterSizeSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizes);

        //Spinners Resources
        dataAdapterTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterSizeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapters to spinners
        typeSpinner.setAdapter(dataAdapterTypeSpinner);
        sizeSpinner.setAdapter(dataAdapterSizeSpinner);

        //Loading User's Picture
        String imageLink = PersistentData.getAdopterImage(this);
        Picasso.get().load(imageLink).into(profilePic);

        loadPets(setFilters(selectedType,selectedSize));

        //Event listeners for Spinners
        //Type Spinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Make the type filter here
                selectedType = typeSpinner.getSelectedItem().toString().trim();
                loadPets(setFilters(selectedType,selectedSize));
                adapter.stopListening();
                adapter.startListening();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Size Spinner
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSize = sizeSpinner.getSelectedItem().toString().trim();
                loadPets(setFilters(selectedType,selectedSize));
                adapter.stopListening();
                adapter.startListening();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadPets(setFilters("All types","All sizes"));
                adapter.stopListening();
                adapter.startListening();
                return false;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadPets(setSearch(query.toUpperCase()));
                adapter.stopListening();
                adapter.startListening();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    public Query setFilters(String type, String size){
        Query query = firebaseFirestore.collection("Pets").whereNotEqualTo("petName","No pet found");

        if(type != "All types" && size != "All sizes"){
            query = query.whereEqualTo("type",type).whereEqualTo("size",size);
        }else if(size != "All sizes"){
            query = query.whereEqualTo("size",size);
        }else if(type != "All types"){
            query = query.whereEqualTo("type",type);
        }
        return query;
    }

    public Query setSearch(String searchBreed){
        Query query = firebaseFirestore.collection("Pets").orderBy("breed").startAt(searchBreed).endAt(searchBreed+"\uf8ff");
        return query;
    }

    //Looks for pets using the filters
    public void loadPets(Query finalQuery){

        Query query = finalQuery;

        //This is the code that builds the cells of each pet
        FirestoreRecyclerOptions<PetInShelterModel> options = new FirestoreRecyclerOptions.Builder<PetInShelterModel>()
                .setQuery(query, PetInShelterModel.class)
                .build();

        //Adapter
        adapter =  new FirestoreRecyclerAdapter<PetInShelterModel, PetsGridViewHolder>(options) {

            @NonNull
            @NotNull
            @Override
            public PetsGridViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_petgrid,parent, false);
                return new PetsGridViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull PetsGridViewHolder holder, int position, @NonNull @NotNull PetInShelterModel model) {

                Picasso.get().load(model.getPetImage()).into(holder.petPhoto);

                //On Click Photo
                holder.petPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(FindPetActivity.this, PetDetailsActivity.class);
                        intent.putExtra("petId",model.getPetID());
                        intent.putExtra("shelterId", model.getShelterId());
                        intent.putExtra("petBreed", model.getBreed());
                        startActivity(intent);

                    }
                });
            }
        };

        findAllPetsGridRV.setLayoutManager(new GridLayoutManager(this,3));
        findAllPetsGridRV.setAdapter(adapter);

    }


    private void resetSpinners() {
        sizeSpinner.setSelection(0,true);
        typeSpinner.setSelection(0);
    }



    //View Holder
    private class PetsGridViewHolder extends RecyclerView.ViewHolder {

        private ImageView petPhoto;

        public PetsGridViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            petPhoto = itemView.findViewById(R.id.squareImagePet);

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
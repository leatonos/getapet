package com.example.capstone_devwolfs_get_a_pet.Activities.Adopters;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.Models.PetInShelterModel;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Wishlist extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView petsListWishlist;
    private FirestoreRecyclerAdapter adapter;
    public String[] elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        firebaseFirestore = FirebaseFirestore.getInstance();
        petsListWishlist = findViewById(R.id.wishlistRecyclerView);

        //Retrieve the wishlist from the user
        String wishlistStr = PersistentData.getAdopterWishlist(this);
        elements = wishlistStr.split("\\s*,\\s*");

        if(wishlistStr.equals("") || wishlistStr == null){
            Toast.makeText(getApplicationContext(), "No pets Found", Toast.LENGTH_LONG).show();
            elements = new String[]{"noPet"};
            petsListWishlist.setVisibility(View.INVISIBLE);
        }

        //This is the query
        Query query = firebaseFirestore.collection("Pets").whereIn(FieldPath.documentId(),Arrays.asList(elements));


        //This is the code that builds the cells of each pet
        FirestoreRecyclerOptions<PetInShelterModel> options = new FirestoreRecyclerOptions.Builder<PetInShelterModel>()
                .setQuery(query, PetInShelterModel.class)
                .build();

        //Adapter
        String[] finalElements = elements;
        adapter =  new FirestoreRecyclerAdapter<PetInShelterModel, PetsViewHolderWishlist>(options) {

            @NonNull
            @NotNull
            @Override
            public PetsViewHolderWishlist onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item,parent, false);
                return new PetsViewHolderWishlist(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull PetsViewHolderWishlist holder, int position, @NonNull @NotNull PetInShelterModel model) {
                holder.petName.setText(model.getPetName());
                Picasso.get().load(model.getPetImage()).into(holder.petPhoto);


                holder.petPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), PetDetailsActivity.class);
                        intent.putExtra("petId",model.getPetID());
                        intent.putExtra("shelterId",model.getShelterId());
                        intent.putExtra("petBreed", model.getBreed());
                        startActivity(intent);

                    }
                });

                holder.deletePet.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {

                        String finalResult = "";
                        List<String> list = Arrays.asList(finalElements);
                        ArrayList<String> listOfString = new ArrayList<String>(list);
                        listOfString.removeIf( name -> name.equals(model.getPetID()));
                        StringBuilder sb = new StringBuilder();
                        for (String s: listOfString) {
                            sb.append(s);
                            sb.append(",");
                        }
                        if(sb.toString().length() == 0){
                        finalResult = "";
                        }else {
                            finalResult = sb.substring(0,sb.toString().length()-1);
                        }

                        String userID = PersistentData.getAdopterId(getApplicationContext());
                        DocumentReference selectedUser = firebaseFirestore.collection("Adopters").document(userID);

                        //updating PersistentData
                        PersistentData.updateAdopterWishlist(getApplicationContext(),finalResult);
                        //updating Database wishlist
                        selectedUser.update("wishlist",finalResult);
                        Toast.makeText(getApplicationContext(), "Removing pet from the wishlist", Toast.LENGTH_LONG).show();

                        //Refreshing the activity
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                    }
                });
                
            }
        };

        petsListWishlist.setHasFixedSize(true);
        petsListWishlist.setLayoutManager(new LinearLayoutManager(this));
        petsListWishlist.setAdapter(adapter);

    }

    //View Holder
    private class PetsViewHolderWishlist extends RecyclerView.ViewHolder {

        private TextView petName;
        private ImageView petPhoto;
        private ImageButton deletePet;

        public PetsViewHolderWishlist(@NonNull @NotNull View itemView) {
            super(itemView);

            petName = itemView.findViewById(R.id.petNameWishList);
            petPhoto = itemView.findViewById(R.id.petImageWishList);
            deletePet = itemView.findViewById(R.id.deleteFromWishlistBtn);

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
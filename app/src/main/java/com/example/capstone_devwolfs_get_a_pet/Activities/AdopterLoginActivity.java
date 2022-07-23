package com.example.capstone_devwolfs_get_a_pet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_devwolfs_get_a_pet.InternalData.PersistentData;
import com.example.capstone_devwolfs_get_a_pet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdopterLoginActivity extends AppCompatActivity {

    EditText userName,password;
    Button login;
    TextView registerLnkAdopter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Adopters");

    public static String adopterUsName = "";
    public static String adopterName = "";
    public static String adopterID = "";
    public static String adopterAddress = "";
    public static String adopterDescription = "";
    public static String adopterEmail = "";
    public static String adopterPhone = "";
    public static String adopterPhoto = "";
    public static String adopterWishlist = "";

    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopter_login);


        userName = findViewById(R.id.adopterUsername);
        password = findViewById(R.id.adopterPass);
        login = findViewById(R.id.adopterLoginBtn);
        registerLnkAdopter = findViewById(R.id.registerAdopterLk);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = userName.getText().toString().trim();
                String passWord = password.getText().toString().trim();

                userRef.whereEqualTo("adopterEmail",username).whereEqualTo("adopterPassword",passWord).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(queryDocumentSnapshots.size() == 1) {

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                adopterUsName = documentSnapshot.getString("adopterEmail");
                                adopterName = documentSnapshot.getString("adopterName");
                                adopterID = documentSnapshot.getId();
                                adopterAddress = documentSnapshot.getString("adopterAddress");
                                adopterDescription = documentSnapshot.getString("adopterDescription");
                                adopterEmail = documentSnapshot.getString("adopterEmail");
                                adopterName = documentSnapshot.getString("adopterName");
                                adopterPhone = documentSnapshot.getString("adopterPhone");
                                adopterPhoto = documentSnapshot.getString("adopterImage");
                                adopterWishlist = documentSnapshot.getString("wishlist");

                                //Saving Adopter data inside the internal storage of the phone
                                PersistentData.saveAdopterData(adopterID,adopterAddress,adopterDescription,adopterEmail,adopterName,adopterPhone,adopterPhoto,adopterWishlist,v.getContext());

                                Intent intent = new Intent(v.getContext(), AdopterDashboardActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();

                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });

        registerLnkAdopter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdopterAuthActivity.class);
                startActivity(intent);
            }
        });

    }
}
package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

public class ShelterDisplayActivity extends AppCompatActivity {

    private FirebaseFirestore mDB;
    private ListView customListView;
    private String FIRE_LOG = "Fire_log";
    private static final List<Shelter> shelters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_display);

        mDB = FirebaseFirestore.getInstance();

       // initShelterList(shelters, mDB);

        mDB.collection("shelters")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Shelter shelter = document.toObject(Shelter.class);
                            shelters.add(shelter);
                        }

//                        ListAdapter shelterAdapter = new CustomShelterAdapter(getApplicationContext(), shelters);
//                        customListView = (ListView) findViewById(R.id.shelterList);
//                        customListView.setAdapter(shelterAdapter);
//
//                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
//                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
//                                intent.putExtra("shelter", shelter);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
                    } else {
                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                    }
                }
            });
        Log.d("success1", String.valueOf(shelters.size()));
        ListAdapter shelterAdapter = new CustomShelterAdapter(getApplicationContext(), shelters);
        customListView = (ListView) findViewById(R.id.shelterList);
        customListView.setAdapter(shelterAdapter);

        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                intent.putExtra("shelter", shelter);
                startActivity(intent);
                finish();
            }
        });

    }

//    public void initShelterList(final List<Shelter> shelters, FirebaseFirestore mDB) {
//        mDB.collection("shelters")
//            .get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        List<Shelter> temp = new ArrayList<>();
//                        for (DocumentSnapshot document : task.getResult()) {
//                            Shelter shelter = document.toObject(Shelter.class);
//                            Log.d("dayin", shelter.getAddress());
//                            shelters.add(shelter);
//                        }
//                          // Log.d("success1", String.valueOf(shelters.size()));
//                       // shelters = temp;
//                    } else {
//                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
//                    }
//                }
//            });
//    }

}

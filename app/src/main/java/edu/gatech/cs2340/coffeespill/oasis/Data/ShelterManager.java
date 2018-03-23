package edu.gatech.cs2340.coffeespill.oasis.Data;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;

/**
 * Created by andrew_chang on 2018-03-22.
 */

public class ShelterManager {
    private FirebaseFirestore mDB;
    private String FIRE_LOG = "Fire_log";
    List<Shelter> shelters = new ArrayList<>();

    public ShelterManager() {
        this.mDB = FirebaseFirestore.getInstance();
    }

    public List<Shelter> getData() {

        mDB.collection("shelters").orderBy("id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Shelter shelter = document.toObject(Shelter.class);
                                if (shelter == null) {
                                    System.out.println("null");
                                } else {
                                    if (!shelters.contains(shelter)) {
                                        shelters.add(shelter);
                                        System.out.println("added " + shelter.getName());
                                    }
                                }
                            }
                        } else {
                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
        return shelters;
    }

}

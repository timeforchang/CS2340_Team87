package edu.gatech.cs2340.coffeespill.oasis.Data;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
        shelters = getAll();
    }

    public List<Shelter> getAll() {
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
                                        System.out.println("added " + shelter.getName() + " " + shelter.getCapacity());
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

    public void check(final Shelter s, final int checkNum) {
        mDB.collection("shelters").whereEqualTo("id", s.getId())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (DocumentSnapshot document : task.getResult()) {
                               Shelter dShelter = document.toObject(Shelter.class);
                               if (dShelter == null) {
                                   System.out.println("found shelter is null");
                               } else {
                                   int newCap = dShelter.getCapacity() - checkNum;
                                   Map<String, Object> data = new HashMap<>();
                                   data.put("capacity", newCap);
                                   mDB.collection("shelters").document(Integer.toString(dShelter.getId()))
                                           .set(data, SetOptions.merge());
                                   s.setCapacity(newCap);
                                   shelters.set(shelters.indexOf(dShelter), dShelter);
                               }
                           }
                       }
                   }
               });
    }

    public void out(final Shelter s) {
        mDB.collection("shelters").whereEqualTo("id", s.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Shelter dShelter = document.toObject(Shelter.class);
                                if (dShelter == null) {
                                    System.out.println("found shelter is null");
                                } else {
                                    int newCap = dShelter.getCapacity() + 1;
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("capacity", newCap);
                                    mDB.collection("shelters").document(Integer.toString(dShelter.getId()))
                                            .set(data, SetOptions.merge());
                                    s.setCapacity(newCap);
                                    shelters.set(shelters.indexOf(dShelter), dShelter);
                                }
                            }
                        }
                    }
                });
    }

    public List<Shelter> refresh() {
        shelters.clear();
        shelters = getAll();
        return shelters;
    }
}

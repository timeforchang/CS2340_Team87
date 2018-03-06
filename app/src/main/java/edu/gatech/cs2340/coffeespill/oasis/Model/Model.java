package edu.gatech.cs2340.coffeespill.oasis.Model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private ArrayList<Shelter> shelters = null;
    private FirebaseFirestore db;

    private Model() {
        shelters = new ArrayList<Shelter>();
        db = db = FirebaseFirestore.getInstance();

        loadData();
    }

    private void loadData() {
        db.collection("shelters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Shelter shelter = document.toObject(Shelter.class);
                        System.out.println(shelter);
                        shelters.add(shelter);
                    }
                }
            }
        });
                    //shelters = (ArrayList<DocumentSnapshot>) future.getResult().getDocuments();
        System.out.println(shelters.size());

        for (Shelter shelter : shelters) {
            System.out.println(shelter.getId());
        }
    }

    public ArrayList<Shelter> getShelters() {
        return shelters;
    }
}

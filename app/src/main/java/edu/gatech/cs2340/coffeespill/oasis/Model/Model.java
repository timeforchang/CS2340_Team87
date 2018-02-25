package edu.gatech.cs2340.coffeespill.oasis.Model;

import android.support.annotation.NonNull;
import android.support.compat.BuildConfig;

import com.google.android.gms.tasks.Task;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.api.core.ApiFuture;

import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private ArrayList<DocumentSnapshot> shelters = null;
    private FirebaseFirestore db;

    private Model() {
        shelters = new ArrayList<DocumentSnapshot>();
        db = db = FirebaseFirestore.getInstance();

        loadData();
    }

    private void loadData() {
        Task<com.google.firebase.firestore.QuerySnapshot> future = db.collection("shelters").get();

        shelters = (ArrayList<DocumentSnapshot>) future.getResult().getDocuments();

        for (DocumentSnapshot shelter : shelters) {
            System.out.println(shelter.getId() + " => " + shelter.toObject(Shelter.class));
        }
    }
}

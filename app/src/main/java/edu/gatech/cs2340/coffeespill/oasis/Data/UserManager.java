package edu.gatech.cs2340.coffeespill.oasis.Data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Model.User;

/**
 * Created by andrew_chang on 2018-03-23.
 */

public class UserManager {
    private FirebaseFirestore mDB;
    private FirebaseAuth auth;
    private String FIRE_LOG = "Fire_log";
    User curUser;

    public UserManager() {
        this.mDB = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        curUser = getData();
    }

    public User getData() {
        System.out.println(auth.getCurrentUser().getEmail());
        mDB.collection("users").whereEqualTo("_contact", auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if (user == null) {
                                    System.out.println("null");
                                } else {
                                    curUser = user;
                                    System.out.println(user);
                                    System.out.println(auth.getCurrentUser().getEmail());
                                }
                            }
                        } else {
                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
        return curUser;
    }
}

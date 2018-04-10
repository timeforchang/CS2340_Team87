package edu.gatech.cs2340.coffeespill.oasis.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.coffeespill.oasis.model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.model.User;

/**
 * Created by andrew_chang on 2018-03-23.
 */

@SuppressWarnings("ALL")
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

    @SuppressWarnings("ConstantConditions")
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

    @SuppressWarnings("ConstantConditions")
    public void check(final Shelter s, final int checkNum) {
        mDB.collection("users").whereEqualTo("_contact", auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                User dUser = document.toObject(User.class);
                                if (dUser == null) {
                                    System.out.println("found user is null");
                                } else {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("_checked", true);
                                    data.put("_checkedNum", checkNum);
                                    data.put("_checkedSID", s.getId());
                                    mDB.collection("users").document(dUser.get_id())
                                            .set(data, SetOptions.merge());
                                    curUser.set_checkedSID(s.getId());
                                    curUser.set_checked(true);
                                    curUser.set_checkedNum(checkNum);
                                    System.out.println(curUser);
                                }
                            }
                        }
                    }
                });
    }

    @SuppressWarnings("unused")
    public void out(final Shelter s, final int checkNum) {
        mDB.collection("users").whereEqualTo("_contact", auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                User dUser = document.toObject(User.class);
                                if (dUser == null) {
                                    System.out.println("found user is null");
                                } else {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("_checked", false);
                                    data.put("_checkedNum", 0);
                                    data.put("_checkedSID", -1);
                                    mDB.collection("users").document(dUser.get_id())
                                            .set(data, SetOptions.merge());
                                    curUser.set_checkedSID(-1);
                                    curUser.set_checked(false);
                                    curUser.set_checkedNum(0);
                                }
                            }
                        }
                    }
                });
    }

    public User refresh() {
        curUser = getData();
        return curUser;
    }
}

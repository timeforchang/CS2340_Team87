package edu.gatech.cs2340.coffeespill.oasis.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.model.Model;
import edu.gatech.cs2340.coffeespill.oasis.model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-03-22.
 */

@SuppressWarnings("ALL")
public class ShelterListActivity extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener {
    //private TextView tvOut;
    Model model = Model.getInstance();
    List<Shelter> s;
    List<Shelter> f = new ArrayList<>();
    private FirebaseFirestore mDB;
    private String FIRE_LOG = "Fire_log";

    CustomShelterAdapter adapter;

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    List<Category> categories = new ArrayList<>(Arrays.asList(new Category("Male Only"),
            new Category("Female Only"), new Category("Families w/ Newborns"), new Category("Children")
            , new Category("Young Adults"), new Category("Anyone")));
    SidebarAdapter sideA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_list);
        mDB = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }

        s = model.getShelters();
        System.out.println("called");

        Toast.makeText(this, "Got database", Toast.LENGTH_SHORT).show();

        if (!s.isEmpty()) {
            adapter = new CustomShelterAdapter(this, s);
            RecyclerView recView = findViewById(R.id.rvShelters);
            recView.setAdapter(adapter);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        sideA = new SidebarAdapter(this, categories);
        this.mDrawerList = findViewById(R.id.left_drawer);
        this.mDrawerList.setAdapter(sideA);

        final Handler timerHandler;
        timerHandler = new Handler();

        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                // Here you can update your adapter data
                s = model.getShelters();
                System.out.println("tick");
                adapter.notifyDataSetChanged();
                timerHandler.postDelayed(this, 2000); //run every 2 seconds
            }
        };

        timerHandler.postDelayed(timerRunnable, 2000); //Start timer after 2 secs
    }

    @Override
    public void onResume() {
        super.onResume();
        model.refresh();
        s = model.getShelters();
        System.out.println("called");
        for (Shelter shelter : s) {
            System.out.print("name: " + shelter.getName() + " | ");
            System.out.println("cap: " + shelter.getCapacity());
        }
        adapter = new CustomShelterAdapter(this, s);
        RecyclerView recView = findViewById(R.id.rvShelters);
        recView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String string) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String string) {
                f.clear();
                for (Shelter shelter : s) {
                    if (shelter.getName().contains(string)) {
                        if (!f.contains(shelter)) {
                            f.add(shelter);
                        }
                    }
                }
                adapter = new CustomShelterAdapter(getApplicationContext(), f);
                RecyclerView recView = findViewById(R.id.rvShelters);
                recView.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.profile:
                //displayCategories();
                //Log.d("test", mDrawerLayout.toString());
                startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                return true;
            case R.id.search:
                return true;
            case R.id.filter:
                //displayCategories();
                //Log.d("test", mDrawerLayout.toString());
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
            case R.id.refresh:
                model.refresh();
                s = model.getShelters();
                adapter = new CustomShelterAdapter(this, s);
                RecyclerView recView = findViewById(R.id.rvShelters);
                recView.setAdapter(adapter);
                break;
            case R.id.maps:
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int pos = mDrawerList.getPositionForView(compoundButton);
        if (pos != ListView.INVALID_POSITION) {
            Category c = categories.get(pos);
            c.setSelected(b);
            if (b) {
                switch (pos) {
                    case 0:
                        mDB.collection("shelters").whereEqualTo("restrictions", "men")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                f.add(shelter);
                                            }

                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        break;
                    case 1:
                        mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        break;
                    case 2:
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ newborns")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                f.add(shelter);
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        categories.get(5).setSelected(false);
                        sideA = new SidebarAdapter(this, categories);
                        //Log.d("test", sideA.toString());
                        this.mDrawerList = findViewById(R.id.left_drawer);
                        this.mDrawerList.setAdapter(sideA);
                        break;
                    case 3:
                        mDB.collection("shelters").whereEqualTo("restrictions", "children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                f.add(shelter);
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "children/young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ children under 5")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        categories.get(5).setSelected(false);
                        sideA = new SidebarAdapter(this, categories);
                        //Log.d("test", sideA.toString());
                        this.mDrawerList = findViewById(R.id.left_drawer);
                        this.mDrawerList.setAdapter(sideA);
                        break;
                    case 4:
                        mDB.collection("shelters").whereEqualTo("restrictions", "children/young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        categories.get(5).setSelected(false);
                        sideA = new SidebarAdapter(this, categories);
                        //Log.d("test", sideA.toString());
                        this.mDrawerList = findViewById(R.id.left_drawer);
                        this.mDrawerList.setAdapter(sideA);
                        mDB.collection("shelters").whereEqualTo("restrictions", "young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        categories.get(5).setSelected(false);
                        sideA = new SidebarAdapter(this, categories);
                        //Log.d("test", sideA.toString());
                        this.mDrawerList = findViewById(R.id.left_drawer);
                        this.mDrawerList.setAdapter(sideA);
                        break;
                    default:
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ newborns")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                f.add(shelter);
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                f.add(shelter);
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "children/young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ children under 5")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!f.contains(shelter)) {
                                                    f.add(shelter);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        if (!categories.get(0).isSelected() && !categories.get(1).isSelected()) {
                            mDB.collection("shelters").whereEqualTo("restrictions", "men")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    Shelter shelter = document.toObject(Shelter.class);
                                                    if (!f.contains(shelter)) {
                                                        f.add(shelter);
                                                    }
                                                }
                                                adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                                RecyclerView recView = findViewById(R.id.rvShelters);
                                                recView.setAdapter(adapter);
                                            } else {
                                                Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                            }
                                        }
                                    });
                            mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    Shelter shelter = document.toObject(Shelter.class);
                                                    if (!f.contains(shelter)) {
                                                        f.add(shelter);
                                                    }
                                                }
                                                adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                                RecyclerView recView = findViewById(R.id.rvShelters);
                                                recView.setAdapter(adapter);
                                            } else {
                                                Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                            }
                                        }
                                    });
                            for (int i = 0; i < 2; i++) {
                                Category a = categories.get(i);
                                a.setSelected(true);
                            }
                            sideA = new SidebarAdapter(this, categories);
                            //Log.d("test", sideA.toString());
                            this.mDrawerList = findViewById(R.id.left_drawer);
                            this.mDrawerList.setAdapter(sideA);
                        }
                        for (int i = 2; i < 5; i++) {
                            Category a = categories.get(i);
                            a.setSelected(false);
                        }
                        sideA = new SidebarAdapter(this, categories);
                        //Log.d("test", sideA.toString());
                        this.mDrawerList = findViewById(R.id.left_drawer);
                        this.mDrawerList.setAdapter(sideA);
                        break;
                }
            } else {
                switch (pos) {
                    case 0:
                        mDB.collection("shelters").whereEqualTo("restrictions", "men")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        if (!categories.get(0).isSelected() && !categories.get(1).isSelected() && !categories.get(2).isSelected() &&
                                !categories.get(3).isSelected() && !categories.get(4).isSelected() && !categories.get(5).isSelected()) {
                            f.clear();
                            displayAll();
                        }
                        break;
                    case 1:
                        mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        if (!categories.get(0).isSelected() && !categories.get(1).isSelected() && !categories.get(2).isSelected() &&
                                !categories.get(3).isSelected() && !categories.get(4).isSelected() && !categories.get(5).isSelected()) {
                            f.clear();
                            displayAll();
                        }
                        break;
                    case 2:
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ newborns")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        if (!categories.get(0).isSelected() && !categories.get(1).isSelected() && !categories.get(2).isSelected() &&
                                !categories.get(3).isSelected() && !categories.get(4).isSelected() && !categories.get(5).isSelected()) {
                            f.clear();
                            displayAll();
                        }
                        break;
                    case 3:
                        mDB.collection("shelters").whereEqualTo("restrictions", "children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "children/young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ children under 5")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        if (!categories.get(0).isSelected() && !categories.get(1).isSelected() && !categories.get(2).isSelected() &&
                                !categories.get(3).isSelected() && !categories.get(4).isSelected() && !categories.get(5).isSelected()) {
                            f.clear();
                            displayAll();
                        }
                        break;
                    case 4:
                        mDB.collection("shelters").whereEqualTo("restrictions", "children/young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        if (!categories.get(0).isSelected() && !categories.get(1).isSelected() && !categories.get(2).isSelected() &&
                                !categories.get(3).isSelected() && !categories.get(4).isSelected() && !categories.get(5).isSelected()) {
                            f.clear();
                            displayAll();
                        }
                        break;
                    default:
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ newborns")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "children/young adults")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        mDB.collection("shelters").whereEqualTo("restrictions", "families w/ children under 5")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                int index = f.indexOf(shelter);
                                                if (index >= 0) {
                                                    f.remove(index);
                                                }
                                            }
                                            adapter = new CustomShelterAdapter(getApplicationContext(), f);
                                            RecyclerView recView = findViewById(R.id.rvShelters);
                                            recView.setAdapter(adapter);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        if (!categories.get(0).isSelected() && !categories.get(1).isSelected() && !categories.get(2).isSelected() &&
                                !categories.get(3).isSelected() && !categories.get(4).isSelected() && !categories.get(5).isSelected()) {
                            f.clear();
                            displayAll();
                        }
                        break;
                }
            }
        }
    }

    private void displayAll() {
        mDB.collection("shelters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            adapter = new CustomShelterAdapter(getApplicationContext(), s);
                            RecyclerView recView = findViewById(R.id.rvShelters);
                            recView.setAdapter(adapter);
                        } else {
                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
        Toast.makeText(this, "If shelters do not show up, try refreshing", Toast.LENGTH_SHORT).show();
    }
}

package edu.gatech.cs2340.coffeespill.oasis.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

@SuppressWarnings("ALL")
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, android.widget.CompoundButton.OnCheckedChangeListener {

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    List<Category> categories = new ArrayList<>(Arrays.asList(new Category("Male Only"),
            new Category("Female Only"), new Category("Families w/ Newborns"), new Category("Children")
            , new Category("Young Adults"), new Category("Anyone")));
    SidebarAdapter sideA;
    private GoogleMap mMap;
    private List<Shelter> shelters;
    private List<Shelter> filtered = new ArrayList<>();
    private FirebaseFirestore mDB;
    private String FIRE_LOG = "Fire_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDB = FirebaseFirestore.getInstance();
        Model model = Model.getInstance();
        shelters = model.getShelters();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        sideA = new SidebarAdapter(this, categories);
        this.mDrawerList = findViewById(R.id.left_drawer);
        this.mDrawerList.setAdapter(sideA);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        for (Shelter shelter : shelters) {
            LatLng temp = new LatLng(shelter.getLatitude(), shelter.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(temp)
                    .title(shelter.getName()))
                    .setSnippet("Address: " + shelter.getAddress());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(33.7490, -84.3830) , 12.0f) );
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filter) {
            mDrawerLayout.openDrawer(mDrawerList);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
                                        } else {
                                            //noinspection ConstantConditions
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        break;
                    case 1:
                        mDB.collection("shelters").whereEqualTo("restrictions", "women/children")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @SuppressWarnings("ConstantConditions")
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Shelter shelter = document.toObject(Shelter.class);
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                filtered.add(shelter);
                                            }
                                            reDisplayMarkers(filtered);
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        categories.get(5).setSelected(false);
                        sideA = new SidebarAdapter(this, categories);
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
                                                filtered.add(shelter);
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                filtered.add(shelter);
                                            }
                                            reDisplayMarkers(filtered);
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
                                                filtered.add(shelter);
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                if (!filtered.contains(shelter)) {
                                                    filtered.add(shelter);
                                                }
                                            }
                                            reDisplayMarkers(filtered);
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
                                                    if (!filtered.contains(shelter)) {
                                                        filtered.add(shelter);
                                                    }
                                                }
                                                reDisplayMarkers(filtered);
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
                                                    if (!filtered.contains(shelter)) {
                                                        filtered.add(shelter);
                                                    }
                                                }
                                                reDisplayMarkers(filtered);
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
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
                                                int index = filtered.indexOf(shelter);
                                                if (index >= 0) {
                                                    filtered.remove(index);
                                                }
                                            }
                                            if (filtered.size() == 0) {
                                                reDisplayMarkers(shelters);
                                            } else {
                                                reDisplayMarkers(filtered);
                                            }
                                        } else {
                                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                        }
                                    }
                                });
                        break;
                }
            }

            Toast.makeText(this, "Filter selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void reDisplayMarkers(List<Shelter> filtered) {
        mMap.clear();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        for (Shelter shelter : filtered) {
            LatLng temp = new LatLng(shelter.getLatitude(), shelter.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(temp)
                    .title(shelter.getName()))
                    .setSnippet("Tel: " + shelter.getPhone());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(filtered.get(0).getLatitude(), filtered.get(0).getLongitude())));
    }


}

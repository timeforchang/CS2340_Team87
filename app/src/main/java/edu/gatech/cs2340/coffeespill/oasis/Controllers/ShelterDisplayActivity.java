package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Model.Model;
import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

public class ShelterDisplayActivity extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener{

    private FirebaseFirestore mDB;
    private ListView customListView;
    private String FIRE_LOG = "Fire_log";
    private List<Shelter> shelters = new ArrayList<>();
    private List<Shelter> filtered = new ArrayList<>();
    private ListAdapter shelterAdapter;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    List<Category> categories = new ArrayList<>();
    SidebarAdapter sideA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_display);
        Model model = Model.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        categories = new ArrayList<Category>();
        categories.add(new Category("Male Only"));
        categories.add(new Category("Female Only"));
        categories.add(new Category("Families w/ Newborns"));
        categories.add(new Category("Children"));
        categories.add(new Category("Young Adults"));
        categories.add(new Category("Anyone"));
        sideA = new SidebarAdapter(this, categories);
        //Log.d("test", sideA.toString());
        this.mDrawerList = (ListView) findViewById(R.id.left_drawer);
        this.mDrawerList.setAdapter(sideA);
        //Log.d("test", mDrawerLayout.toString());


        setSupportActionBar(toolbar);

        mDB = FirebaseFirestore.getInstance();
        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), shelters);
        customListView = (ListView) findViewById(R.id.shelterList);

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
                            shelterAdapter = new CustomShelterAdapter(getApplicationContext(), shelters);
                            customListView = (ListView) findViewById(R.id.shelterList);
                            customListView.setAdapter(shelterAdapter);
                            customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.d("test", "3");
                                    Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                    Log.d("test", "4");
                                    Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                    intent.putExtra("shelter", shelter);
                                    Log.d("test", "5");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filter) {
            //displayCategories();
            //Log.d("test", mDrawerLayout.toString());
            mDrawerLayout.openDrawer(mDrawerList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int pos = mDrawerList.getPositionForView(compoundButton);
        Log.d("test", Integer.toString(pos));
        if(pos != ListView.INVALID_POSITION) {
            Category c = categories.get(pos);
            c.setSelected(b);
            if(b) {
                if (pos == 0) {
                    mDB.collection("shelters").whereEqualTo("restrictions", "men")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Shelter shelter = document.toObject(Shelter.class);
                                            filtered.add(shelter);
                                            Log.d("test", shelter.getName());
                                        }
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else if (pos == 1) {
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
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else if (pos == 2) {
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
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else if(pos == 3) {
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
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
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
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
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
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
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
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                    }
                                }
                            });

                } else if(pos == 4) {
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
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else {
                    mDB.collection("shelters")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        filtered.clear();
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Shelter shelter = document.toObject(Shelter.class);
                                            filtered.add(shelter);
                                        }
                                        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                        customListView = (ListView) findViewById(R.id.shelterList);
                                        customListView.setAdapter(shelterAdapter);
                                        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Log.d("test", "3");
                                                Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                                Log.d("test", "4");
                                                Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                                intent.putExtra("shelter", shelter);
                                                Log.d("test", "5");
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                    }
                                }
                            });
                }
            }

            Toast.makeText(this, "Filter selected", Toast.LENGTH_SHORT).show();
        }

    }
}

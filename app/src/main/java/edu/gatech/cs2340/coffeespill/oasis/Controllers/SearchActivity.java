package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.gatech.cs2340.coffeespill.oasis.Model.Model;
import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-03-08.
 */

public class SearchActivity extends AppCompatActivity {
    private FirebaseFirestore mDB;
    private ListView customListView;
    private String FIRE_LOG = "Fire_log";
    private List<Shelter> searched = new ArrayList<>();
    private ListAdapter shelterAdapter;
    EditText editSearch;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Model model = Model.getInstance();

        mDB = FirebaseFirestore.getInstance();
        shelterAdapter = new CustomShelterAdapter(getApplicationContext(), searched);
        customListView = (ListView) findViewById(R.id.searchList);
        editSearch = (EditText) findViewById(R.id.searchBar);
        searchBtn = (Button) findViewById(R.id.search_btn);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT);

        mDB.collection("shelters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Shelter shelter = document.toObject(Shelter.class);
                                searched.add(shelter);
                            }
                            shelterAdapter = new CustomShelterAdapter(getApplicationContext(), searched);
                            customListView = (ListView) findViewById(R.id.searchList);
                            customListView.setAdapter(shelterAdapter);
                            customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                    Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                    intent.putExtra("shelter", shelter);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = editSearch.getText().toString();
                mDB.collection("shelters").orderBy("name").startAt(searchText).endAt(searchText + "\uf8ff")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Shelter> filtered = new ArrayList<>();
                                    for (DocumentSnapshot document : task.getResult()) {
                                        Shelter shelter = document.toObject(Shelter.class);
                                        filtered.add(shelter);
                                    }
                                    shelterAdapter = new CustomShelterAdapter(getApplicationContext(), filtered);
                                    customListView = (ListView) findViewById(R.id.searchList);
                                    customListView.setAdapter(shelterAdapter);
                                    customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            Shelter shelter = (Shelter) adapterView.getItemAtPosition(i);
                                            Intent intent = new Intent(getApplicationContext(), ShelterDescriptionActivity.class);
                                            intent.putExtra("shelter", shelter);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    Log.d(FIRE_LOG, "Error getting documents: " + task.getException().getMessage());
                                }
                            }
                        });
            }
        });

        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }
            }
        });
    }
}

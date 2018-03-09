package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by China on 03/08/2018.
 */

public class SearchByNameActivity extends AppCompatActivity {
    private Button search;
    private DatabaseReference ShelterDatabase;
    private TextView shelterName;
    private RecyclerView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ShelterDatabase = FirebaseDatabase.getInstance().getReference("Shelter");
        search = (Button) findViewById(R.id.search);
        shelterName = (TextView) findViewById(R.id.shelterName);
        searchResult = (RecyclerView)findViewById(R.id.searchResult);
        searchResult.setHasFixedSize(true);
        searchResult.setLayoutManager(new LinearLayoutManager(this));


        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firebaseUserSearch();
            }
        });
     }

    private void firebaseUserSearch() {
        FirebaseRecyclerAdapter<shelters, ShelterViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<shelters, ShelterViewHolder>(
                        shelters.class,
                R.layout.list_view,
                ShelterViewHolder.class,
                ShelterDatabase
        )
        {
          @Override
            protected void populateViewHolder(ShelterViewHolder viewHolder, shelters model, int position) {
              viewHolder.setDetails(getApplicationContext(), model.getName());
          }
        };
        searchResult.setAdapter(firebaseRecyclerAdapter);
    }


    public static class ShelterViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ShelterViewHolder(View itemView) {
             super(itemView);
             view = itemView;
         }

         public void setDetails(Context ctx, String name) {
             ImageView shelter_name = (ImageView) view.findViewById(R.id.shelterName);

             Glide.with(ctx).load(name).into(shelter_name);

         }
     }
}

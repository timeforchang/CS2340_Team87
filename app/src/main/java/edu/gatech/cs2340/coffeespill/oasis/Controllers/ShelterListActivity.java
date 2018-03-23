package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.gatech.cs2340.coffeespill.oasis.Model.Model;
import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-03-22.
 */

public class ShelterListActivity extends AppCompatActivity {
    //private TextView tvOut;
    Model model = Model.getInstance();
    List<Shelter> s;
    private FirebaseFirestore mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_list);
        mDB = FirebaseFirestore.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

//        Collections.sort(s, new Comparator<Shelter>() {
//            @Override
//            public int compare(Shelter shelter, Shelter t1) {
//                return shelter.getName().compareTo(t1.getName());
//            }
//        });
        Toast.makeText(this, "Got database", Toast.LENGTH_SHORT).show();

        final CustomShelterAdapter adapter = new CustomShelterAdapter(this, s);

        RecyclerView recView = (RecyclerView) findViewById(R.id.rvShelters);
        recView.setAdapter(adapter);

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
        if (id == R.id.profile) {
            //displayCategories();
            //Log.d("test", mDrawerLayout.toString());
            startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
            return true;
        } else if (id == R.id.search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
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
}

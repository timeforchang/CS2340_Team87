package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.gatech.cs2340.coffeespill.oasis.Model.Model;
import edu.gatech.cs2340.coffeespill.oasis.Model.User;
import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class UserInfoActivity extends AppCompatActivity {
    private Button logout;
    private TextView userEmail;
    private FirebaseAuth auth;
    Model model = Model.getInstance();
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        logout = (Button) findViewById(R.id.logout);
        u = model.getUser();

        userEmail = (TextView) findViewById(R.id.userEmail);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            //userEmail.setText(u.get_contact());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogout();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserInfoActivity.this, ShelterListActivity.class));
        finish();
    }

    private void userLogout() {
        auth.signOut();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(UserInfoActivity.this, HomeScreenActivity.class));
            finish();
        }
    }
}

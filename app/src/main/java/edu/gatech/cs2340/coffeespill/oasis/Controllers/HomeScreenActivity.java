package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import edu.gatech.cs2340.coffeespill.oasis.R;

public class HomeScreenActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(HomeScreenActivity.this, UserInfoActivity.class);
            startActivity(intent);
            finish();
        }

        Button login = (Button) findViewById(R.id.gotologin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button register = (Button) findViewById(R.id.gotoregister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

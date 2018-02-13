package edu.gatech.cs2340.coffeespill.oasis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        goInsideApplication();
    }

    private void goInsideApplication() {
        Button logIn = (Button) findViewById(R.id.logIn);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getText().toString().equals("user")) {
                    Toast.makeText(HomeScreen.this, "Wrong username, " +
                            "try again please!", Toast.LENGTH_LONG).show();
                } else if (!password.getText().toString().equals("pass")) {
                    Toast.makeText(HomeScreen.this, "Wrong password, " +
                            "try again please!", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(HomeScreen.this,
                            InsideApplicationActivity.class));
                }
            }
        });
    }
}

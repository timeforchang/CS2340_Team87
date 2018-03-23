package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText loginEmail, loginPass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPass = (EditText) findViewById(R.id.loginPass);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, ShelterListActivity.class);
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snack = Snackbar.make(findViewById(R.id.loginScreen), "logging you in...", Snackbar.LENGTH_LONG);
                System.out.println("logged in");
                snack.show();
                loginUser(loginEmail.getText().toString(), loginPass.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUser(String email, final String pass) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Snackbar snack = Snackbar.make(findViewById(R.id.loginScreen), "login unsuccessful", Snackbar.LENGTH_LONG);
                    snack.show();
                } else {
                    startActivity(new Intent(LoginActivity.this, UserInfoActivity.class));
                    finish();
                }
            }
        });
    }
}

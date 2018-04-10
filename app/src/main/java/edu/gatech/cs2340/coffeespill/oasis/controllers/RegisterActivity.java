package edu.gatech.cs2340.coffeespill.oasis.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.coffeespill.oasis.model.User;
import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText registerEmail, registerPass, registerName;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = findViewById(R.id.register);
        registerEmail = findViewById(R.id.registerEmail);
        registerPass = findViewById(R.id.registerPass);
        typeSpinner = findViewById(R.id.userTypeSpinner);
        registerName = findViewById(R.id.registerName);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, User.userTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(registerEmail.getText().toString(), registerPass.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerUser(final String email, final String pass) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    System.out.println("error signing up");
                    Snackbar snack = Snackbar.make(findViewById(R.id.registerScreen), "register unsuccessful", Snackbar.LENGTH_LONG);
                    snack.show();
                } else {
                    System.out.println("sign up successful");
                    Map<String, Object> newUser = new HashMap<>();
                    newUser.put("_id", auth.getCurrentUser().getUid());
                    newUser.put("_username", registerName.getText().toString());
                    newUser.put("_type", typeSpinner.getSelectedItem());
                    newUser.put("_locked", false);
                    newUser.put("_contact", email);
                    newUser.put("_pWord", pass);
                    newUser.put("_checked", false);
                    newUser.put("_checkedNum", 0);
                    newUser.put("_checkedSID", -1);

                    db.collection("users").document(auth.getCurrentUser().getUid()).set(newUser);

                    Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}

package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.coffeespill.oasis.Model.Model;
import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.Model.User;
import edu.gatech.cs2340.coffeespill.oasis.R;

public class ShelterDescriptionActivity extends AppCompatActivity {

    private TextView dName, dCap, dAdd, dPhone, dRestr, dNotes, dMess;
    private Button checkIn, checkOut;
    Model model = Model.getInstance();
    Shelter shelter;
    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_description);

        shelter = getIntent().getExtras().getParcelable(CustomShelterAdapter.SHELTER_KEY);
        curUser = model.getUser();
        System.out.println(curUser);
        if (shelter == null) {
            throw new AssertionError("Null data received");
        }

        dName = (TextView) findViewById(R.id.descName);
        dCap = (TextView) findViewById(R.id.descCap);
        dAdd = (TextView) findViewById(R.id.descAddress);
        dPhone = (TextView) findViewById(R.id.descPhone);
        dRestr = (TextView) findViewById(R.id.descRestr);
        dNotes = (TextView) findViewById(R.id.descNotes);
        checkIn = (Button) findViewById(R.id.checkInButton);
        checkOut = (Button) findViewById(R.id.checkOutButton);
        dMess = (TextView) findViewById(R.id.checkedMessage);

        dName.setText(shelter.getName());
        dCap.setText("Space Remaining: " + Integer.toString(shelter.getCapacity()));
        dAdd.setText(shelter.getAddress());
        dPhone.setText(shelter.getPhone());
        dRestr.setText("Restrictions: " + shelter.getRestrictions());
        dNotes.setText("Notes: " + shelter.getNotes());
        checkOut.setEnabled(false);
        dMess.setVisibility(View.INVISIBLE);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pre: " + shelter.getCapacity());
                model.checkout(shelter);
                checkIn.setEnabled(true);
                checkOut.setEnabled(false);
                System.out.println("post: " + shelter.getCapacity());
                dCap.setText("Space Remaining: " + Integer.toString(shelter.getCapacity() + 1));
                Toast.makeText(ShelterDescriptionActivity.this, "Checked Out!", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pre: " + shelter.getCapacity());
                model.checkin(shelter);
                checkIn.setEnabled(false);
                checkOut.setEnabled(true);
                System.out.println("post: " + shelter.getCapacity());
                dCap.setText("Space Remaining: " + Integer.toString(shelter.getCapacity() - 1));
                Toast.makeText(ShelterDescriptionActivity.this, "Checked In!", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        if (curUser.is_checked() && shelter.getCapacity() > 0) {
            checkIn.setEnabled(false);
            if (curUser.get_checkedSID() == shelter.getId()) {
                checkOut.setEnabled(true);
            } else {
                dMess.setVisibility(View.VISIBLE);
            }
        } else if (!curUser.is_checked() && shelter.getCapacity() > 0) {
            checkIn.setEnabled(true);
        } else {
            checkIn.setEnabled(false);
            checkOut.setEnabled(false);
            dMess.setText("There are no more vacancies left in this shelter!");
            dMess.setVisibility(View.VISIBLE);
        }
    }
}

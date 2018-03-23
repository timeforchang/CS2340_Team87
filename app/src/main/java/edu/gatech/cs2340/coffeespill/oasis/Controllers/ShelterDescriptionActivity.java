package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

public class ShelterDescriptionActivity extends AppCompatActivity {

    private TextView dName, dCap, dAdd, dPhone, dRestr, dNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_description);

        Shelter shelter = getIntent().getExtras().getParcelable(CustomShelterAdapter.SHELTER_KEY);
        if (shelter == null) {
            throw new AssertionError("Null data received");
        }

        dName = (TextView) findViewById(R.id.descName);
        dCap = (TextView) findViewById(R.id.descCap);
        dAdd = (TextView) findViewById(R.id.descAddress);
        dPhone = (TextView) findViewById(R.id.descPhone);
        dRestr = (TextView) findViewById(R.id.descRestr);
        dNotes = (TextView) findViewById(R.id.descNotes);

        dName.setText(shelter.getName());
        dCap.setText("Space Remaining: " + Integer.toString(shelter.getCapacity()));
        dAdd.setText(shelter.getAddress());
        dPhone.setText(shelter.getPhone());
        dRestr.setText("Restrictions: " + shelter.getRestrictions());
        dNotes.setText("Notes: " + shelter.getNotes());
    }
}

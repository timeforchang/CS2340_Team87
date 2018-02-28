package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

public class ShelterDescriptionActivity extends AppCompatActivity {

    private TextView shelterDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_description);

        Bundle bundle = getIntent().getExtras();
        Shelter shelter = (bundle != null) ? (Shelter) bundle.getParcelable("shelter") : null;
        String detail = "Id: " + shelter.getId() + "\nName: " + shelter.getName() + "\nCapacity: "
                + shelter.getCapacity() + "\nGender: " + shelter.getRestrictions()
                + "\nLongitude " + shelter.getLongitude() + "\nLatitude: "
                + shelter.getLongitude() + "\nAddress: "
                + shelter.getAddress() + "\nSpecial Notes: " + shelter.getNotes()
                + "\n Phone Number: " + shelter.getPhone();

        shelterDetails = (TextView) findViewById(R.id.shelterDetails);
        shelterDetails.setText((shelterDetails.getText().toString()));


    }
}

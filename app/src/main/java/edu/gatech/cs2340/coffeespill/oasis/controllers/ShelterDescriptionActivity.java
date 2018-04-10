package edu.gatech.cs2340.coffeespill.oasis.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.cs2340.coffeespill.oasis.model.Model;
import edu.gatech.cs2340.coffeespill.oasis.model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.model.User;
import edu.gatech.cs2340.coffeespill.oasis.R;

@SuppressWarnings("ALL")
public class ShelterDescriptionActivity extends AppCompatActivity {

    private TextView dCap;
    private TextView dTV;
    private Button checkIn, checkOut;
    private NumberPicker np;
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

        TextView dName = findViewById(R.id.descName);
        dCap = findViewById(R.id.descCap);
        TextView dAdd = findViewById(R.id.descAddress);
        TextView dPhone = findViewById(R.id.descPhone);
        TextView dRestr = findViewById(R.id.descRestr);
        TextView dNotes = findViewById(R.id.descNotes);
        checkIn = findViewById(R.id.checkInButton);
        checkOut = findViewById(R.id.checkOutButton);
        TextView dMess = findViewById(R.id.checkedMessage);
        np = findViewById(R.id.checkinNum);
        dTV = findViewById(R.id.checkNum);

        dName.setText(shelter.getName());
        dCap.setText(getString(R.string.descCap, shelter.getCapacity()));
        dAdd.setText(shelter.getAddress());
        dPhone.setText(shelter.getPhone());
        dRestr.setText(getString(R.string.descResc, shelter.getRestrictions()));
        dNotes.setText(getString(R.string.descNotes, shelter.getNotes()));
        checkOut.setEnabled(false);
        dMess.setVisibility(View.INVISIBLE);

        np.setMinValue(1);
        np.setMaxValue(shelter.getCapacity());
        np.setWrapSelectorWheel(true);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pre: " + shelter.getCapacity());
                model.checkout(shelter, curUser.get_checkedNum());
                dCap.setText(getString(R.string.descCap, shelter.getCapacity() + curUser.get_checkedNum()));

                checkIn.setEnabled(true);
                checkOut.setEnabled(false);
                np.setVisibility(View.VISIBLE);
                dTV.setVisibility(View.VISIBLE);
                System.out.println("post: " + shelter.getCapacity());
                Toast.makeText(ShelterDescriptionActivity.this, "Checked Out!", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pre: " + shelter.getCapacity());
                model.checkin(shelter, np.getValue());
                curUser = model.getUser();
                //model.refresh();
                dCap.setText(getString(R.string.descCap, shelter.getCapacity() - np.getValue()));

                checkIn.setEnabled(false);
                checkOut.setEnabled(true);
                np.setVisibility(View.GONE);
                dTV.setVisibility(View.GONE);
                System.out.println("post: " + shelter.getCapacity());
                Toast.makeText(ShelterDescriptionActivity.this, "Checked In!", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        if (curUser.is_checked() && shelter.getCapacity() >= 0) {
            checkIn.setEnabled(false);
            np.setVisibility(View.GONE);
            dTV.setVisibility(View.GONE);
            if (curUser.get_checkedSID() == shelter.getId()) {
                checkOut.setEnabled(true);
            } else {
                dMess.setVisibility(View.VISIBLE);
            }
        } else if (!curUser.is_checked() && shelter.getCapacity() > 0) {
            checkIn.setEnabled(true);
        } else {
            np.setVisibility(View.GONE);
            dTV.setVisibility(View.GONE);
            checkIn.setEnabled(false);
            checkOut.setEnabled(false);
            dMess.setText(getString(R.string.emptyError));
            dMess.setVisibility(View.VISIBLE);
        }
    }
}

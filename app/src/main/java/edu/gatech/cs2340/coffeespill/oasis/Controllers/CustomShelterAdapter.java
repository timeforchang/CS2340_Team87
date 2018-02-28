package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;


public class CustomShelterAdapter extends ArrayAdapter<Shelter> {

    public CustomShelterAdapter(@NonNull Context context, List<Shelter> shelters) {
        super(context, R.layout.custom_shelter_row, shelters);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_shelter_row, parent, false);
        String shelterName = getItem(position).getName();

        TextView itemName = (TextView) customView.findViewById(R.id.shelterName);
        itemName.setText(shelterName);

        return convertView;
    }
}

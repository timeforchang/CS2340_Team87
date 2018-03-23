package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;


public class CustomShelterAdapterListView extends ArrayAdapter<Shelter> {

    List<Shelter> mShelters;
    LayoutInflater mInflater;

    public CustomShelterAdapterListView(@NonNull Context context, List<Shelter> shelters) {
        super(context, R.layout.custom_shelter_row, shelters);
        mShelters = shelters;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_shelter_row, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.shelterName);
        TextView capacity = (TextView) convertView.findViewById(R.id.shelterCapacity);
        TextView address = (TextView) convertView.findViewById(R.id.shelterAddress);

        Shelter s = mShelters.get(position);

        name.setText(s.getName());
        capacity.setText(Integer.toString(s.getCapacity()));
        address.setText(s.getAddress());

        return convertView;
    }
}

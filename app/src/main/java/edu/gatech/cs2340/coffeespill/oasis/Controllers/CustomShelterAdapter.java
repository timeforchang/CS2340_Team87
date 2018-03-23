package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-03-22.
 */

public class CustomShelterAdapter extends RecyclerView.Adapter<CustomShelterAdapter.ViewHolder> {
    public static final String SHELTER_KEY = "item_key";
    private List<Shelter> mShelters;
    private Context mContext;

    public CustomShelterAdapter(Context context, List<Shelter> s) {
        this.mShelters = s;
        this.mContext = context;
    }

    @Override
    public CustomShelterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View shelterView = inflater.inflate(R.layout.custom_shelter_row, parent, false);
        ViewHolder vh = new ViewHolder(shelterView);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomShelterAdapter.ViewHolder holder, int position) {
        final Shelter shelter = mShelters.get(position);

        if (shelter != null) {
            holder.sName.setText(shelter.getName());
            holder.sCapacity.setText(Integer.toString(shelter.getCapacity()));
            holder.sAddress.setText(shelter.getAddress());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ShelterDescriptionActivity.class);
                    intent.putExtra(SHELTER_KEY, shelter);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mShelters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sName;
        public TextView sCapacity;
        public TextView sAddress;
        public View mView;

        public ViewHolder(View shelterView) {
            super(shelterView);

            sName = (TextView) shelterView.findViewById(R.id.shelterName);
            sCapacity = (TextView) shelterView.findViewById(R.id.shelterCapacity);
            sAddress = (TextView) shelterView.findViewById(R.id.shelterAddress);
            mView = shelterView;
        }
    }
}

package edu.gatech.cs2340.coffeespill.oasis.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-03-07.
 */

class Category {
    private String name;
    private boolean selected = false;

    public Category(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

class SidebarAdapter extends ArrayAdapter<Category> {
    @SuppressWarnings("CanBeFinal")
    private Context con;
    @SuppressWarnings("CanBeFinal")
    private List<Category> list;

    public SidebarAdapter(@NonNull Context context, List<Category> filters) {
        super(context, R.layout.drawer_list_item, filters);
        this.con = context;
        this.list = filters;
    }

    private static class filterHolder {
        TextView category;
        CheckBox check;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        filterHolder holder = new filterHolder();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            v = inflater.inflate(R.layout.drawer_list_item, parent, false);
            holder.category = v.findViewById(R.id.category);
            holder.check = v.findViewById(R.id.checkbox);

            holder.check.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) con);
            v.setTag(holder);
        } else {
            holder = (filterHolder) v.getTag();
        }
        final Category c = list.get(position);
        holder.category.setText(c.getName());
        holder.check.setChecked(c.isSelected());
        holder.check.setTag(c);

        final filterHolder finalHolder = holder;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalHolder.check.setChecked(!c.isSelected());
            }
        });

        return v;
    }
}
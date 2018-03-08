package edu.gatech.cs2340.coffeespill.oasis.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Model.Shelter;
import edu.gatech.cs2340.coffeespill.oasis.R;

/**
 * Created by andrew_chang on 2018-03-07.
 */

class Category {
    String name;
    boolean selected = false;

    public Category(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

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

public class SidebarAdapter extends ArrayAdapter<Category> {
    private Context con;
    private List<Category> list;

    public SidebarAdapter(@NonNull Context context, List<Category> filters) {
        super(context, R.layout.drawer_list_item, filters);
        this.con = context;
        this.list = filters;
    }

    private static class filterHolder {
        public TextView category;
        public CheckBox check;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        filterHolder holder = new filterHolder();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.drawer_list_item, null);
            holder.category = (TextView) v.findViewById(R.id.category);
            holder.check = (CheckBox) v.findViewById(R.id.checkbox);

            holder.check.setOnCheckedChangeListener((ShelterDisplayActivity) con);
        } else {
            holder = (filterHolder) v.getTag();
        }
        Category c = list.get(position);
        holder.category.setText(c.getName());
        holder.check.setChecked(c.isSelected());
        holder.check.setTag(c);

        return v;
    }
}

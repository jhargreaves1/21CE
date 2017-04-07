package com.app.csubmobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.app.csubmobile.MapActivity;
import com.app.csubmobile.R;
import com.app.csubmobile.data.BuildingItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuildingListAdapter extends ArrayAdapter<BuildingItem> {

    Context context;
    int resource, textViewResourceId;
    List<BuildingItem> items, tempItems, suggestions;

    public BuildingListAdapter(Context context, int resource, int textViewResourceId, List<BuildingItem> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<BuildingItem>(items); // this makes the difference.
        suggestions = new ArrayList<BuildingItem>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.building_item, parent, false);
        }
        BuildingItem people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BuildingItem selectedBuilding = items.get(position);
                if (selectedBuilding != null) {
                    Intent i = new Intent(context, MapActivity.class);
                    i.putExtra("Building", (Serializable) selectedBuilding);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }
                //Toast.makeText(context, selectedBuilding.getName()+"", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((BuildingItem) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (BuildingItem people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<BuildingItem> filterList = (ArrayList<BuildingItem>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (BuildingItem people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };


}

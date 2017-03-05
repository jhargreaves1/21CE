package com.app.csubmobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.app.csubmobile.R;
import com.app.csubmobile.data.BuildingItem;
import java.util.List;


public class BuildingListAdapter extends BaseAdapter {

    private List<BuildingItem> buildingItems;
    private Activity activity;
    private LayoutInflater inflater;

    public BuildingListAdapter(Activity activity, List<BuildingItem> buildingItems) {
        this.activity = activity;
        this.buildingItems = buildingItems;
    }
    @Override
    public int getCount() {
        return buildingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return buildingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.building_item, null);

        TextView buildingName = (TextView) convertView.findViewById(R.id.building_name);
        TextView created_date = (TextView) convertView.findViewById(R.id.building_abbrev);

        BuildingItem item = buildingItems.get(position);

        buildingName.setText(item.getName());
        created_date.setText(item.getAbbrev());
        return convertView;
    }
}

package com.app.csubmobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.csubmobile.R;
import com.app.csubmobile.data.DiningItem;

import java.util.List;

/**
 * Created by Jonathan on 4/6/2017.
 */

public class DiningListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<DiningItem> feedItems;

    public DiningListAdapter(Activity activity, List<DiningItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.dining_item, null);

        TextView dining_name = (TextView) convertView.findViewById(R.id.dining_name);
        TextView dining_description = (TextView) convertView.findViewById(R.id.dining_description);
        ImageView dining_icon = (ImageView) convertView.findViewById(R.id.dining_icon);
        DiningItem item = feedItems.get(position);

        dining_name.setText(item.getName());
        dining_description.setText(item.getDescription());
        switch (item.getIcon()) {
            case "Burger":
                dining_icon.setImageResource(R.drawable.burger_icon);
                break;
            case "Coffee":
                dining_icon.setImageResource(R.drawable.coffee_icon);
                break;
            case "Tray":
                dining_icon.setImageResource(R.drawable.cafeteria_icon);
                break;
            case "Sandwich":
                dining_icon.setImageResource(R.drawable.sandwich_icon);
                break;
            case "ForkKnife":
                dining_icon.setImageResource(R.drawable.forkknife_icon);
                break;
            default:
                dining_icon.setImageResource(R.drawable.forkknife_icon);
                break;
        }

        return convertView;
    }
}


package com.app.csubmobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class TransViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap< >();

    public TransViewAdapter(Context context,
                            ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView bus;
        //TextView info;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.trans_listview_item, parent, false);
        resultp = data.get(position);
        bus = (TextView) itemView.findViewById(R.id.route);
        //info = (TextView) itemView.findViewById(R.id.routeinfo);
        bus.setText(resultp.get(TransportationActivity.TITLE));
        //info.setText(resultp.get(TransportationActivity.LINK));
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                resultp = data.get(position);
                Intent intent = new Intent(context, RouteView.class);
                intent.putExtra("bus", resultp.get(TransportationActivity.TITLE));
                //intent.putExtra("info", resultp.get(TransportationActivity.LINK));
                context.startActivity(intent);
            }
        });
        return itemView;
    }
}

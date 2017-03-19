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
    ArrayList<HashMap<String, String>> routes;
    //ArrayList<HashMap<String, String>> links;
    ArrayList<String> links = new ArrayList<String>();
    HashMap<String, String> result_r = new HashMap< >();
    //HashMap<String, String> result_l = new HashMap< >();
    String result_l ;

    public TransViewAdapter(Context context,
                            ArrayList<HashMap<String, String>> routelist,
                            ArrayList<String> urlList
                            ) {
        this.context = context;
        routes = routelist;
        links = urlList;

    }

    @Override
    public int getCount() {
        return routes.size();
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
        TextView link;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.trans_listview_item, parent, false);
        result_r = routes.get(position);
        result_l = links.get(position);
        bus = (TextView) itemView.findViewById(R.id.title);
        link = (TextView) itemView.findViewById(R.id.link);
        bus.setText(result_r.get(TransportationActivity.TITLE));
        link.setText(result_l);
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                result_r = routes.get(position);
                Intent intent = new Intent(context, RouteView.class);
                intent.putExtra("bus", result_r.get(TransportationActivity.TITLE));
                intent.putExtra("link", links.get(position));
                context.startActivity(intent);
            }
        });
        return itemView;
    }
}

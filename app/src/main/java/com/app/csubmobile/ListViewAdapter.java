package com.app.csubmobile;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
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
        TextView title;
        TextView link;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.news_listview_item, parent, false);
        resultp = data.get(position);
        title = (TextView) itemView.findViewById(R.id.title);
        //link = (TextView) itemView.findViewById(R.id.link);
        title.setText(resultp.get(NewsActivity.TITLE));
        //link.setText(resultp.get(NewsActivity.LINK));
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleNewsView.class);
                intent.putExtra("title", resultp.get(NewsActivity.TITLE));
                intent.putExtra("link", resultp.get(NewsActivity.LINK));
                context.startActivity(intent);
            }
        });
        return itemView;
    }
}

package com.app.csubmobile.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.csubmobile.FeedItem;
import com.app.csubmobile.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

/**
 * Created by qpngd on 3/26/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<FeedItem> feedItems;
    Context context;

    public MyAdapter(Context context, ArrayList<FeedItem> feedItems) {
        this.feedItems = feedItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_events_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // cardview animation
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);

        FeedItem current = feedItems.get(position);
        holder.Title.setText(current.getTitle());
        holder.Description.setText(current.getDescription());
        holder.Date.setText(current.getPubDate());
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Description,Date;
        ImageView Thumbnail;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title_text);
            Description = (TextView) itemView.findViewById(R.id.description_text);
            Date = (TextView) itemView.findViewById(R.id.date_text);
            // cardview animation
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}

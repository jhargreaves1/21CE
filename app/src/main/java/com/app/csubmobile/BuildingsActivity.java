package com.app.csubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.app.csubmobile.adapter.BuildingListAdapter;
import com.app.csubmobile.data.BuildingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ponism on 2/19/2017.
 */

public class BuildingsActivity extends AppCompatActivity {
    private ListView listView;
    private BuildingListAdapter listAdapter;
    private List<BuildingItem> buildingItem = new ArrayList<BuildingItem>();
    private Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        i = getIntent();
        buildingItem = (List<BuildingItem>) i.getSerializableExtra("Buildings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buildings_layout);
        listView = (ListView)findViewById(R.id.buildings_list);

        listAdapter = new BuildingListAdapter(this, buildingItem);
        listView.setAdapter(listAdapter);
    }
}

package com.app.csubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.app.csubmobile.adapter.BuildingListAdapter;
import com.app.csubmobile.data.BuildingItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListBuildings extends AppCompatActivity {
    private ListView listView;
    private BuildingListAdapter listAdapter;
    private List<BuildingItem> buildingItems = new ArrayList<>();
    private Intent i;
    //private ListView list_buildings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        i = getIntent();
        buildingItems = (List<BuildingItem>) i.getSerializableExtra("Buildings");
        Collections.sort(buildingItems);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buildings);
        listView = (ListView)findViewById(R.id.list_building);
        //txtSearch = (AutoCompleteTextView) findViewById(R.id.txt_search);
        //buildingItems = retrievePeople();
        listAdapter = new BuildingListAdapter(this, R.layout.activity_list_buildings, R.id.lbl_name, buildingItems);
        listView.setAdapter(listAdapter);

    }

}

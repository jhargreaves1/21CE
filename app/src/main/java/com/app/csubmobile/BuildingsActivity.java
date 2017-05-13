package com.app.csubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
    private List<BuildingItem> buildingItems = new ArrayList<>();
    private Intent i;
    AutoCompleteTextView txtSearch;
    EditText buildingSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        i = getIntent();
        buildingItems = (List<BuildingItem>) i.getSerializableExtra("Buildings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buildings_layout);
        //listView = (ListView)findViewById(R.id.buildings_list);
        txtSearch = (AutoCompleteTextView) findViewById(R.id.txt_search);
        //buildingItems = retrievePeople();
        listAdapter = new BuildingListAdapter(this, R.layout.buildings_layout, R.id.lbl_name, buildingItems);
        txtSearch.setAdapter(listAdapter);
    }


}

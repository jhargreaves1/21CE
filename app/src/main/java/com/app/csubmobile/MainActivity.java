package com.app.csubmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    public static List<String> directory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        Intent i = new Intent(getApplicationContext(), RegistrationService.class);
        startService(i);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //get_data(URL_FACULTY, directory);
        /*
          Creating all buttons instances
          */
        // Dashboard News button
        Button btn_news = (Button) findViewById(R.id.btn_news);

        // Dashboard Map button
        Button btn_map = (Button) findViewById(R.id.btn_map);

        // Dashboard Directory button
        Button btn_directory = (Button) findViewById(R.id.btn_directory);

        // Dashboard Dining button
        Button btn_dining = (Button) findViewById(R.id.btn_dining);

        // Dashboard Events button
        Button btn_events = (Button) findViewById(R.id.btn_events);

        // Dashboard Social Media button
        Button btn_social_media = (Button) findViewById(R.id.btn_social_media);

        // Dashboard transportation button
        Button btn_transportation = (Button) findViewById(R.id.btn_transportation);

        // Dashboard Schedule button
        Button btn_schedule = (Button) findViewById(R.id.btn_schedule);

        // Dashboard Blackboard/Moodle button
        Button btn_blackboard_moodle = (Button) findViewById(R.id.btn_blackboard_moodle);

        /*
          Handling all button click events
          */

        // Listening to News Feed button click
        btn_news.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(i);
            }
        });

        // Listening Map button click
        btn_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Map Feed Screen
                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(i);
            }
        });

        // Listening Directory button click
        btn_directory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Directory Screen
                Intent i = new Intent(getApplicationContext(), DirectoryActivity.class);
                startActivity(i);
            }
        });

        // Listening to Dining button click
        btn_dining.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Dining Screen
                Intent i = new Intent(getApplicationContext(), DiningActivity.class);
                startActivity(i);
            }
        });

        // Listening to Events button click
        btn_events.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Events Screen
                Intent i = new Intent(getApplicationContext(), EventsActivity.class);
                startActivity(i);
            }
        });

        // Listening to Social Media button click
        btn_social_media.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Social Media Screen
                Intent i = new Intent(getApplicationContext(), SocialMediaActivity.class);
                startActivity(i);
            }
        });

        // Listening to Transportation button click
        btn_transportation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Transportation Screen
                Intent i = new Intent(getApplicationContext(), TransportationActivity.class);
                startActivity(i);
            }
        });

        // Listening to Schedule button click
        btn_schedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Schedule Screen
                Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(i);
            }
        });

        // Listening to Moodle button click
        btn_blackboard_moodle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Blackboard/Moodle Screen
                Intent i = new Intent(getApplicationContext(), Blackboard_MoodleActivity.class);
                startActivity(i);
            }
        });
    }

    
}

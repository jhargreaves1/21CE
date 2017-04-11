package com.app.csubmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.csubmobile.EndpointsActivity.URL_FACULTY;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    public static List<String> directory = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        Intent i = new Intent(getApplicationContext(), RegistrationService.class);
        startService(i);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        get_data(URL_FACULTY, directory);
        /**
         * Creating all buttons instances
         * */
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

        /**
         * Handling all button click events
         * */

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

    private void get_data(String url, final List list) {
        pDialog.setMessage("Loading data ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONArray jObj = new JSONArray(response);
                    JSONObject json = null;
                    final String[] name = new String[jObj.length()];
                    for (int i = 0; i < jObj.length(); i++) {
                        json = jObj.getJSONObject(i);
                        String title = (json.isNull("faculty_title")) ? null : json.getString("faculty_title");
                        String fullname = title + " " + json.getString("faculty_fname") + " " + json.getString("faculty_lname");
                        name[i] = fullname;
                    }
                    if (list.isEmpty()) {
                        for (int i = 0; i < name.length; i++) {
                            list.add(name[i]);
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(MainActivity.this, "Failure getting data from server", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
